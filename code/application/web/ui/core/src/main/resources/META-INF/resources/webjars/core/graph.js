
(function() {

    var module = angular.module('restgraph', []);

    module.provider('RestGraph', function() {
        function findLink(name, data) {
            if("link" in data) {
                for(var i = 0; i < data.link.length; i++) {
                    var link = data.link[i];
                    if(link.rel === name) {
                        return link;
                    }
                }
            }
        }

        function Node($q, $http, $cache, meta, startURL, parent, startData) {
            this.$q = $q;
            this.$http = $http;
            this.$cache = $cache;
            this._meta = meta;
            this._startURL = startURL;
            this._parent = parent;
            this._data = {};
            this._options = [];
            this._children = [];
            this._isarray = false;
            this._datalevel = 'link';

            if(angular.isDefined(startData)) {
                this._data = startData;
                this._isarray = angular.isArray(startData);
            }
            this.$cache.put(startURL, this);
            if(angular.isUndefined(meta.$type)) {
                this._meta.$type = function() {
                    var typeIndex = 0;
                    if(angular.isDefined(meta.mediaType) &&
                            (typeIndex=meta.mediaType.toLowerCase().indexOf("type")) != -1) {
                        return meta.mediaType.toLowerCase().substring(typeIndex+5).trim();
                    }
                    if(angular.isDefined(meta.$source)) {
                        return meta.$source;
                    }
                    return meta.rel;
                }

            }
        };

        Object.defineProperties(Node.prototype, {
            "parent": {
                get: function() { return this._parent }
            },
            "root": {
                get: function() {
                    var par = this.parent;
                    if (par == null) {
                        return this;
                    }
                    while (angular.isDefined(par.parent)) {
                        par = par.parent;
                    }
                    return par;
                }
            },
            "url": {
                get: function() { return this._startURL}
            },
            "data": {
                get: function() { return this._data }
            },
            "links": {
                get: function() { return this._children }
            },
            "meta": {
                get: function() { return this._meta }
            }
        });

        Node.prototype.isArray = function() {
            return this._isarray;
        };
        Node.prototype.canGet = function() {
            return this._options.indexOf('GET') > -1;
        };
        Node.prototype.canRemove = function() {
            return this._options.indexOf('DELETE') > -1;
        };
        Node.prototype.canUpdate = function() {
            return this._options.indexOf('PUT') > -1
                    || this._options.indexOf('PATCH') > -1;
        };
        Node.prototype.canCreate = function() {
            return this._options.indexOf('POST') > -1
                    || this._options.indexOf('PATCH') > -1;
        };
        Node.prototype.$nodifyArray = function(dataarray) {
            var children = [];

            for ( var i = 0; i < dataarray.length; i++) {
                var data = dataarray[i];
                var d = this.$q.defer();
                children.push(d.promise);

                var resolve = function(defer) {
                    return function(node) {
                        node.$discoverChildren()
                        defer.resolve(node);
                    };
                };
                var link = findLink('self', data);
                link.$source = this.meta.rel;

                var node = this.$cache.get(link.href);
                if(angular.isDefined(node)) {
                    if(node._datalevel !== 'full') {
                        node._data = data;
                        node._datalevel = 'list'
                        node.$discoverChildren();
                    }
                    d.resolve(node);
                } else {
                    var node = new Node(
                            this.$q, this.$http, this.$cache, link,
                            link.href, this, data);
                    node.init().then(resolve(d));
                }
            }
            this._data = this.$q.all(children);
        };
        Node.prototype.$discoverChildren = function() {
            var children = [];

            if ("link" in this._data) {
                for ( var i = 0; i < this._data.link.length; i++) {
                    var link = this._data.link[i];
                    var d = this.$q.defer();
                    children.push(d.promise);

                    var resolve = function(defer) {
                        return function(node) {
                            defer.resolve(node);
                        };
                    };
                    var node = this.$cache.get(link.href);
                    if(angular.isDefined(node)) {
                        d.resolve(node);
                    } else {
                        var linkNode = new Node(
                                this.$q, this.$http, this.$cache, link,
                                link.href, this);
                        linkNode.init().then(resolve(d));
                    }
                }
                this._data.$link = this.data.link;
                delete this._data.link;
            }
            this._children = this.$q.all(children);
        };
        Node.prototype.init = function() {
            var self = this;
            var d = this.$q.defer();
            this.$http({
                method : 'OPTIONS',
                url : this._startURL
            }).success(function(data, status, headers, config) {
                self._options = headers("Allow").split(", ");
                d.resolve(self);
            }).error(function(data, status, headers, config) {
                d.reject(self);
            });
            return d.promise;
        };

        Node.prototype.getLink = function(name, func) {
            this.$q.when(this.links, function(links) {
                for( var i = 0; i < links.length; i++) {
                    var link = links[i];
                    if (link.meta.rel === name) {
                        return func(link);
                    }
                }
                return func();
            })
        };

        Node.prototype.bookmark = function(type, id) {
            var self = this;
            var root = this.root;
            // var link = root.getLink("bookmark"); // TODO: lookup bookmark
            // template url from root

            var d = this.$q.defer();
            var url = root.url + "/bookmark/" + type + "/" + id;
            this.$http.get(url).then(function(data) {
                var selfLink = findLink('self', data.data);

                var node = self.$cache.get(selfLink.href);
                if(angular.isDefined(node)) {
                    d.resolve(node);
                } else {
                    var linkNode = new Node(self.$q, self.$http, self.$cache, {
                        rel : type,
                        url : selfLink.href,
                        mediaType : selfLink.mediaType
                    }, selfLink.href, root, data.data);
                    linkNode.init().then(function(node) {
                        node.$discoverChildren();
                        d.resolve(node);
                    });
                }
            });
            return d.promise;
        };

        Node.prototype.get = function() {
            var self = this;
            var d = this.$q.defer();
            this.$http.get(this._startURL).then(function(data) {
                if(angular.isArray(data.data)) {
                    self._isarray = true;
                    self.$nodifyArray(data.data);
                }
                else {
                    if(!angular.equals(data.data, "")) {
                        self._data = data.data;
                    } else {
                        self._data = {}
                    }
                    self.$discoverChildren();
                    self._datalevel = 'full';
                }
                d.resolve(self);
            }, function(data) {
                d.reject(data);
            });
            return d.promise;
        };
        Node.prototype.update = function(updateddata) {
            var self = this;
            var d = this.$q.defer();
            this.$http.put(this._startURL, updateddata).then(function(data) {
                d.resolve(self.get());
            }, function(data) {
                d.reject(data);
            });
            return d.promise;
        };
        Node.prototype.add = function(adddata) {
            var self = this;
            var d = this.$q.defer();
            this.$http({method:'PATCH', url:this._startURL, data:adddata}).then(function(data) {
                d.resolve(self.get());
            }, function(data) {
                d.reject(data);
            });
            return d.promise;
        };
        Node.prototype.remove = function() {
            var self = this;
            var d = this.$q.defer();
            this.$http['delete'](this._startURL).then(function(data) {
                self._data = {};
                d.resolve(self.parent);
            }, function(data) {
                d.reject(data);
            });
            return d.promise;
        };
        Node.prototype.create = function(updateddata) {
            var self = this;
            var d = this.$q.defer();
            this.$http.post(this._startURL, updateddata).then(
                    function(data) {
                        var location = data.headers('Location');
                        var node = self.$cache.get(location);
                        if(angular.isDefined(node)) {
                            d.resolve(node);
                        } else {
                            var node = new Node(self.$q, self.$http, self.$cache, self.meta,
                                    data.headers('Location'), self);
                            node.init().then(function(res) {
                                d.resolve(res);
                            });
                        }
                    }, function(data) {
                        self._data = data.data;
                        d.reject(self);
                    });
            return d.promise;
        };

        this.$get = function($q, $http, $cacheFactory) {
            return function(baseURL) {
                return new Node($q, $http, $cacheFactory('node'), {
                    href : baseURL,
                    rel : 'root'
                }, baseURL);
            };
        };

    });

})();