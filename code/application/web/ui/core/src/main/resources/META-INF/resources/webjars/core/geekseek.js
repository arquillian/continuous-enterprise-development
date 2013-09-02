function UserCtrl($q, $scope, $rootScope, UserService) {

    $scope.current = UserService.current();

    $scope.view = function() {
        $q.when($scope.current).then(function(current) {
            $rootScope.$broadcast("RootResource.Show", current.user)
        })
    }
}
function MainCtrl($q, $rootScope, $scope, $location, RestGraph, UserService) {

    var MODE_EDIT = "edit";
    var MODE_CREATE = "create";
    var MODE_SHOW = "show";

    $scope.mode = MODE_SHOW;
    $scope.form = {};
    $scope.parents = [];
    $scope.actionLinks = [];
    $scope.userActionLinks = [];

    var resetFormState = function() {
        $scope.form = {};
        $scope.formval = {}
    }

    if (angular.isUndefined($rootScope.root)) {
        $rootScope.root = MainCtrl.resolve.graph(RestGraph, $location);
    }

    if (!($location.path() === '/') && angular.isUndefined($rootScope.resource)) {
        var paths = $location.path().split('/');
        if (paths.length == 4) {
            $q.when($rootScope.root).then(function(root) {
                $scope.resource = root.bookmark(paths[1], paths[2]);
                $scope.mode = paths[3];
            })
        }
    }
    $q.when($rootScope.root).then(function(root){
        if(angular.isUndefined($scope.resource)) {
            $scope.resource = root;
        }
    })

    var setupResource = function(resource) {
        if (angular.isDefined(resource)) {
            $q.when(resource).then(function(value) {
                $scope.parents = $scope.$calcParents(value);
                $scope.userActionLinks = $scope.$calcUserActionLinks(value);
                $scope.actionLinks = $scope.$calcActionLinks(value);

                if ('meta' in value) {
                    $scope.template = value.meta.$type() + '.html';
                    $scope.$bookmark(value);
                }
            });
        }
    }

    $scope.$on('RootResource.Show', function(event, resource) {
        resetFormState()
        $scope.resource = resource;
        $scope.mode = MODE_SHOW;
        event.stopPropagation();
    })

    $scope.$on('RootResource.Edit', function(event, resource) {
        $q.when(resource, function(resource) {
            resetFormState()
            $scope.resource = resource;
            $scope.mode = MODE_EDIT;
            $scope.form = $scope.resource.data
        })
        event.stopPropagation();
    })

    $scope.$on('RootResource.Create', function(event, resource) {
        resetFormState()
        $scope.resource = resource;
        $scope.mode = MODE_CREATE;
        event.stopPropagation();
    })

    $scope.$on('RootResource.Refresh', function(event) {
        $q.when($scope.resource).then(function(resource) {
            resetFormState()
            $scope.resource = resource.get();
            $scope.mode = MODE_SHOW;
        });
        event.stopPropagation();
    })

    // Setup the template for the resource
    var resourceWatcher = $scope.$watch('resource', function(newvalue, oldvalue) {
        setupResource(newvalue);
    });

    $scope.$watch('mode', function(newvalue, oldvalue) {
        if(angular.isDefined($scope.resource)) {
            $scope.$bookmark($scope.resource);
        }
    });

    $scope.$watch('resource.data', function(newvalue, oldvalue) {
        if(angular.isDefined(newvalue)) {
            $scope.isList = $scope.$isListWithData()
            $scope.isSingle = $scope.$isSingleWithData()
        }
    });

    $scope.$bookmark = function(rel) {
        $q.when(rel).then(function(rel) {
            var q = {};
            if (angular.isDefined(rel.data) && angular.isDefined(rel.data.$link)) {
                angular.forEach(rel.data.$link, function(link) {
                    if (angular.isDefined(link.rel) && link.rel === 'bookmark') {
                        var path = link.href.substring(link.href.indexOf("bookmark/") + 8, link.href.length);
                        q.b = path;
                    }
                });
            }
            if (angular.isUndefined(q.b)) {
                q.b = rel.meta.$type();
            }
            if (angular.isDefined($scope.mode)) {
                q.m = $scope.mode;
            }
            //console.log($location.path() + " : " + q.b + "/" + q.m)
            if(!($location.path() === q.b + "/" + q.m)) {
                $location.path(q.b + "/" + q.m);
            }
        });
    };

    $scope.$calcParents = function(resource) {
        var res = [];
        var curr = resource;
        while (angular.isDefined(curr)) {
            if (curr.canGet() && !curr.isArray()) {
                res.push(curr);
            }
            curr = curr.parent;
        }
        return res.reverse();
    };

    $scope.$calcActionLinks = function(resource) {
        var res = [];
        if (angular.isDefined(resource.data)) {
            if (angular.isDefined(resource.links)) {
                $q.when(resource.links).then(function(links) {
                    angular.forEach(links, function(link) {
                        if ('_session_attachments_conference_'.indexOf("_"
                                + link.meta.rel + "_") != -1) {
                            res.push(link);
                        }
                    });
                });
            }
        }
        return res;
    };

    $scope.$calcUserActionLinks = function(resource) {
        var res = [];
        if (angular.isDefined(resource.data)) {
            if (angular.isDefined(resource.links)) {
                $q.when(resource.links).then(function(links) {
                    angular.forEach(links, function(link) {
                        if ('_trackers_speakers_attendees_'.indexOf("_"
                                + link.meta.rel + "_") != -1) {
                            res.push(link);
                        }
                    });
                });
            }
        }
        return res;
    };

    $scope.$isListWithData = function() {
        var d = $q.defer();
        $q.when($scope.resource).then(function(res) {
            $q.when(res.data).then(function(data) {
                if(angular.isDefined(res)) {
                    d.resolve(res.isArray() && data.length > 0);
                } else {
                    d.resolve(false);
                }
            });
        });
        return d.promise;
    };

    $scope.$isSingleWithData = function() {
        var d = $q.defer();
        $q.when($scope.isList).then(function(res) {
            if(res) {
                d.resolve(false);
            }
            else {
                $q.when($scope.resource).then(function(res) {
                    $q.when(res.data).then(function(data) {
                        var hasData = false;
                        for(var prop in data) {
                            if (data.hasOwnProperty(prop)) {
                                hasData = true;
                                break;
                            }
                        }
                        d.resolve(hasData);
                    });
                });
            }
        });
        return d.promise;
    };

    $scope.isEditMode = function() {
        return $scope.mode === MODE_EDIT;
    };

    $scope.isCreateMode = function() {
        return $scope.mode === MODE_CREATE;
    };

    $scope.getViewType = function() {
        if ($scope.isEditMode() || $scope.isCreateMode()) {
            return "form";
        }
        return "display";
    };

    $scope.create = function(link) {
        $scope.$emit("RootResource.Create", link)
    };

    $scope.get = function(link) {
        $scope.mode = MODE_SHOW;
        link.get().then(function(res) {
            $scope.resource = res;
        });
    };

    $scope.addUserTo = function(userAction) {
        $q.when(UserService.current()).then(function(auth) {
            userAction.add(auth.user.meta).then(function() {
                userAction.get();
                //$scope.$emit("RootResource.Refresh")
            });
        });
    };

    $scope.submit = function() {
        var update = function(res) {
            res.get().then(function(resp) {
                $scope.resource = resp;
                $scope.mode = MODE_SHOW;
            });
        };
        var error = function(res) {
            $scope.formval = res.data;
        };
        if ($scope.mode === MODE_EDIT) {
            $scope.resource.update($scope.form).then(update, error);
        } else if ($scope.mode === MODE_CREATE) {
            $scope.resource.create($scope.form).then(update, error);
        }
    };

    $scope.edit = function(node) {
        var res = node;
        if(angular.isUndefined(res)) {
            res = $scope.resource
        }
        $scope.$emit("RootResource.Edit", res)
    };

    $scope.remove = function() {
        $scope.resource.remove().then(function(resp) {
            $scope.mode = MODE_SHOW;
            $scope.form = {};
            // TODO: cheating, parent == the collection
            $scope.resource = $scope.resource.parent.parent;
        });
    };

    $scope.view = function(node) {
        node.getLink('self', function(self){
          self.get().then(function(node) {
              $scope.$emit("RootResource.Show", node)
          })
        });
    };
}

function SubCtrl($q, $scope) {

    var MODE_EDIT = "edit";
    var MODE_CREATE = "create";
    var MODE_SHOW = "show";
    var MODE_NONE = "none";

    $scope.mode = MODE_NONE;
    $scope.template = "";
    $scope.form = {};
    $scope.parents = [];
    $scope.actionlinks = [];

    $scope.$watch('resource', function(newvalue, oldvalue) {
        if(angular.isDefined(newvalue)) {
            $scope.mode = MODE_SHOW;
        } else {
            $scope.mode = MODE_NONE;
        }
    });

    $scope.$watch('resource.data', function(newvalue, oldvalue) {
        if(angular.isDefined(newvalue)) {
            $scope.isList = $scope.$isListWithData()
            $scope.isSingle = $scope.$isSingleWithData()
        }
    });

    $scope.$isListWithData = function() {
        var d = $q.defer();
        $q.when($scope.resource).then(function(res) {
            $q.when(res.data).then(function(data) {
                if(angular.isDefined(res)) {
                    d.resolve(res.isArray() && data.length > 0);
                } else {
                    d.resolve(false);
                }
            });
        });
        return d.promise;
    };

    $scope.$isSingleWithData = function() {
        var d = $q.defer();
        $q.when($scope.isList).then(function(res) {
            if(res) {
                d.resolve(false);
            }
            else {
                $q.when($scope.resource).then(function(res) {
                    $q.when(res.data).then(function(data) {
                        var hasData = false;
                        for(var prop in data) {
                            if (data.hasOwnProperty(prop)) {
                                hasData = true;
                                break;
                            }
                        }
                        d.resolve(hasData);
                    });
                });
            }
        });
        return d.promise;
    };

    $scope.isEditMode = function() {
        return $scope.mode === MODE_EDIT;
    };

    $scope.isNoneMode = function() {
        return $scope.mode === MODE_NONE;
    };

    $scope.isCreateMode = function() {
        return $scope.mode === MODE_CREATE;
    };

    $scope.getViewType = function() {
        if ($scope.isEditMode() || $scope.isCreateMode()) {
            return "form";
        }
        else if ($scope.isNoneMode()) {
            return "none";
        }
        return "display";
    };

    $scope.create = function(link) {
        $scope.$emit("RootResource.Create", link)
    };

    $scope.get = function(link) {
        $scope.mode = MODE_SHOW;
        link.get().then(function(res) {
            $scope.resource = res;
        });
    };

    $scope.submit = function() {
        var update = function(res) {
            res.get().then(function(resp) {
                $scope.resource = resp;
                $scope.mode = MODE_SHOW;
            });
        };
        var error = function(res) {
            $scope.formval = res.data;
        };
        if ($scope.mode === MODE_EDIT) {
            $scope.resource.update($scope.form).then(update, error);
        } else if ($scope.mode === MODE_CREATE) {
            $scope.resource.create($scope.form).then(update, error);
        }
    };

    $scope.edit = function(node) {
        var res = node;
        if(angular.isUndefined(res)) {
            res = $scope.resource
        }
        $scope.$emit("RootResource.Edit", res)
    };

    $scope.remove = function() {
        $scope.resource.remove().then(function(resp) {
            $scope.mode = MODE_SHOW;
            $scope.form = {};
            // TODO: cheating, parent == the collection
            $scope.resource = $scope.resource.parent.parent;
        });
    };

    $scope.view = function(node) {
//        node.getLink('self', function(self){
          node.get().then(function(node) {
              $scope.$emit("RootResource.Show", node)
          })
//        });
    };
}

MainCtrl.resolve = {
    graph : function(RestGraph, $location) {
        // "http://localhost:8080/geekseek/app/root/show"
        // "/root/show"
        var baseURL = $location.absUrl();
        if ($location.path() === '/') {
            baseURL = baseURL.substring(0, baseURL.lastIndexOf('/'));
        } else {
            baseURL = baseURL.replace($location.path(), '');
        }
        baseURL = baseURL.substring(0, baseURL.lastIndexOf('/'));
        baseURL = baseURL + '/api';
        return RestGraph(baseURL).init().then(function(d) {
            return d.get();
        });
    }
};

var gs = angular.module('geekseek', [ 'ngRoute', 'ngSanitize', 'restgraph', 'ui.jq' ], function() {});
gs.filter(
        'navigation',
        function() {
            return function(child) {
                if (angular.isDefined(child) && angular.isDefined(child.meta)) {
                    if (!(child.meta.rel === 'bookmark' && child.meta.rel === 'self')) {
                        return child;
                    }
                }
                return null;
            };
        });
gs.directive(
        'textfield',
        function() {
            return {
                templateUrl : 'webjars/core/textfield.html',
                replace : true,
                transclude : false,
                scope : {
                    id : '@',
                    name : '@',
                    field : '=',
                    error : '=',
                    help : '@'
                },
                restrict : 'E'
            };
        });
gs.directive(
        'textareafield',
        function() {
            return {
                templateUrl : 'webjars/core/textareafield.html',
                replace : true,
                transclude : false,
                scope : {
                    id : '@',
                    name : '@',
                    field : '=',
                    error : '=',
                    help : '@'
                },
                restrict : 'E'
            };
        });
gs.directive(
        'datefield',
        function() {
            return {
                templateUrl : 'webjars/core/datefield.html',
                replace : true,
                transclude : false,
                scope : {
                    id : '@',
                    name : '@',
                    field : '=',
                    error : '=',
                    help : '@'
                },
                restrict : 'E'
            };
        });
gs.directive(
        'dateTimeWidget',
        function() {
            return {
                replace : false,
                transclude : false,
                scope : false,
                restrict : 'C',
                scope : {
                    model : '=ngModel'
                },
                link : function(scope, elem, attrs) {
                    elem.datetimepicker({
                        format : 'mm-dd-yyyy hh:ii',
                        autoclose : true,
                        weekStart : 1,
                        initialDate : angular.isDefined(scope.model) ? new Date(scope.model) : new Date()
                    }).on('changeDate', function(ev) {
                        scope.$apply(function() {
                            scope.model = ev.date.getTime();
                        });
                    });
                    if (angular.isDefined(scope.model)) {
                        elem.datetimepicker('setValue');
                    }
                }
            };
        });
gs.directive(
        'subresource',
        function($q, $compile, $http, $templateCache) {

            var getTemplate = function(template) {
                return $http.get(template, {cache : $templateCache});
            };

            var linker = function($scope, $element, $attrs) {
                var d = $q.defer();
                $scope.resource = d.promise;
                $q.when($scope.parent.links).then(function(links) {
                    angular.forEach(links, function(link) {
                        if (link.meta.rel === $scope.link) {
                            link.get().then(function(res) {
                                d.resolve(res);
                                $scope.template = res.meta.$type() + '.html';;

                                var loader = getTemplate($scope.template);
                                loader.success(function(html) {
                                    $element.html(html);
                                })
                                .then(function(response) {
                                    $element.replaceWith($compile($element.html())($scope));
                                });
                            });
                        }
                    });
                });
            };
            return {
                replace : false,
                transclude : false,
                restrict : 'E',
                scope : {
                    parent : "=",
                    link : "@"
                },
                controller : SubCtrl,
                link : linker
            };
        });
gs.filter(
        'htmlify',
        function($sanitize, $sce) {
            return function(input) {
                return $sce.trustAsHtml($sanitize(input).replace(/\n/g, '<br/>'));
            };
        });
gs.service(
        'UserService',
        function($q, $rootScope) {
            var state = $q.defer();

            this.current = function() {
                return state.promise;
            }

            $rootScope.$watch('root', function(newvalue, oldvalue) {
                if (angular.isUndefined(newvalue)) {
                    return;
                }
                $q.when(newvalue).then(function(root) {
                    $q.when(root.links).then(function(links) {
                        angular.forEach(links, function(link) {
                            if (link.meta.rel === 'whoami') {
                                link.get().then(function(res) {
                                    res.getLink('self', function(link) {
                                        link.get().then(function(user) {
                                            state.resolve({
                                                user: user,
                                                authenticated: true,
                                                knownstate: true
                                            });
                                        })
                                    })
                                }, function() {
                                    state.resolve({
                                        authenticated: false,
                                        knownstate: true
                                    });
                                });
                            }
                        });
                    });
                });
            });
        });
gs.config(
        function($httpProvider, $locationProvider) {

            $locationProvider.html5Mode(true);
            $locationProvider.hashPrefix = '!';

            $httpProvider.defaults.headers.common["Accept"] = "application/vnd.ced+json";
            $httpProvider.defaults.headers.post["Content-Type"] = "application/vnd.ced+json";
            $httpProvider.defaults.headers.put["Content-Type"] = "application/vnd.ced+json";
            $httpProvider.defaults.headers.patch["Content-Type"] = "application/vnd.ced+json";

        });