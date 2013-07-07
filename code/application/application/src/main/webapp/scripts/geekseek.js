
function MainCtrl($q, $rootScope, $scope, $location, graph) {

	var MODE_EDIT = "edit";
	var MODE_CREATE = "create";
	var MODE_SHOW = "show";
	
	console.log("MainCtrl created");

	$scope.mode = MODE_SHOW;
	$scope.form = {};
	$scope.parents = [];
	$scope.actionlinks = [];

	if(angular.isUndefined($rootScope.root)) {
		$rootScope.root = graph;
		$rootScope.resource = graph;
	}

	// Setup the template for the resource
	$rootScope.$watch('resource', function(newvalue, oldvalue) {
		if(angular.isDefined(newvalue)) {
			$q.when(newvalue).then(function(value) {
				if("meta" in value) {
					if("rel" in value.meta) {
						$scope.template = value.meta.rel + ".html";
						$scope.$bookmark(value);
					}
				}
				$scope.parents = $scope.$calcParents(value);
				$scope.actionlinks = $scope.$calcUserActionLinks(value);
			});
		}
	});
	$scope.$watch('mode', function(newvalue, oldvalue) {
		$scope.$bookmark($rootScope.resource);
	});

	if(!($location.path() === '/')) {
		var paths = $location.path().split('/');
		if(paths.length == 4) {
			$rootScope.resource = $rootScope.root.bookmark(paths[1], paths[2]);
			$scope.mode = paths[3];
		}
	}

	$scope.$bookmark = function(rel) {
		$q.when(rel).then(function(rel) {
			var q = {};
			if(angular.isDefined(rel.data) && angular.isDefined(rel.data.$link)) {
				angular.forEach(rel.data.$link, function(link) {
					if(angular.isDefined(link.rel) && link.rel === 'bookmark') {
						var path = link.href.substring(link.href.indexOf("bookmark/")+8, link.href.length);
						q.b = path;
					}
				});
			}
			if(angular.isUndefined(q.b)) {
				q.b = rel.meta.rel;
			}
			if(angular.isDefined($scope.mode)) {
				q.m = $scope.mode;
			}
			$location.path(q.b + "/" + q.m);
		});
	};

	$scope.$calcParents = function(resource) {
		var res = [];
		var curr = resource;
		while(angular.isDefined(curr)) {
			if(curr.canGet()) {
				res.push(curr);
			}
			curr = curr.parent;
		}
		return res.reverse();
	};

	$scope.$calcUserActionLinks = function(resource) {
		var res = [];
		if(angular.isDefined(resource.data)) {
			if(angular.isDefined(resource.links)) {
				$q.when(resource.links).then(function(links) {
					angular.forEach(links, function(link) {
						if('_bookmark_self_parent_'.indexOf("_" + link.meta.rel + "_") == -1) {
							res.push(link);
						}
					});
				});
			}
		}
		return res;
	};

	$scope.isList = function() {
		return angular.isArray($rootScope.resource.data);
	};
	
	$scope.isEditMode = function() {
		return $scope.mode === MODE_EDIT; 
	};

	$scope.isCreateMode = function() {
		return $scope.mode === MODE_CREATE; 
	};
	
	$scope.getViewType = function() {
		if($scope.isEditMode() || $scope.isCreateMode()) {
			return "form";
		}
		return "display";
	};
	
	$scope.create = function(link) {
		$scope.mode = MODE_CREATE;
		$rootScope.resource = link;
	};
	
	$scope.get = function(link) {
		$scope.mode = MODE_SHOW;
		link.get().then(function(res) {
			$rootScope.resource = res;
		});
	};

	$scope.submit = function() {
		var update = function(res) {
			res.get().then(function(resp) {
				$rootScope.resource = resp;
				$scope.mode = MODE_SHOW;
			});
			$scope.formval = {};
			$scope.form = {};
		};
		var error = function(res) {
			$scope.formval = res.data;
		};
		if($scope.mode === MODE_EDIT) {
			$rootScope.resource.update($scope.form).then(update, error);
		}
		else if($scope.mode === MODE_CREATE) {
			$rootScope.resource.create($scope.form).then(update, error);
		}
	};

	$scope.edit = function() {
		$scope.mode = MODE_EDIT;
		$scope.form = $rootScope.resource.data;
	};

	$scope.remove = function() {
		$rootScope.resource.remove().then(function(resp) {
			$scope.mode = MODE_SHOW;
			$scope.form = {};
			$rootScope.resource = $rootScope.resource.parent.parent; // TODO: cheating, parent == the collection
		});
	};
}

MainCtrl.resolve = {
	graph: function(RestGraph, $location) {
		// "http://localhost:8080/geekseek/app/root/show"
		// "/root/show"
		var baseURL = $location.absUrl();
		if($location.path() === '/') {
			baseURL = baseURL.substring(0, baseURL.lastIndexOf('/'));
		} else {
			baseURL = baseURL.replace($location.path(), '');
		}
		baseURL = baseURL.substring(0, baseURL.lastIndexOf('/'));
		baseURL = baseURL + '/api';
		return RestGraph(baseURL).init().then(function(d){
			return d.get();
		});
	}
};

angular.module('geekseek', ['restgraph','$strap.directives'], function() {
})
.filter('navigation', function() {
	return function(child) {
		if(angular.isDefined(child) && angular.isDefined(child.meta)) {
			if(!(child.meta.rel === 'bookmark' || child.meta.rel === 'self')) {
				return child;
			}
		}
		return null;
	};
})
.config(function($routeProvider, $httpProvider, $locationProvider, RestGraphProvider){
	
	$routeProvider.otherwise({
		templateUrl: 'front.html',
		controller: MainCtrl,
		resolve: MainCtrl.resolve,
		reloadOnSearch: false
	});
	
	$locationProvider.html5Mode(true);
	$locationProvider.hashPrefix = '!';

	$httpProvider.defaults.headers.common["Accept"] = "application/vnd.ced+json"; 
	$httpProvider.defaults.headers.post["Content-Type"] = "application/vnd.ced+json";
	$httpProvider.defaults.headers.put["Content-Type"] = "application/vnd.ced+json";

});