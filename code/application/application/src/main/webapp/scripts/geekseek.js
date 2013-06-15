
function MainCtrl($scope, $location, graph) {

	var MODE_EDIT = "edit";
	var MODE_CREATE = "create";
	var MODE_SHOW = "show";
	
	$scope.root = graph;
	// Setup the template for the resource
	$scope.$watch('resource', function(newvalue, oldvalue) {
		if(angular.isDefined(newvalue)) {
			if("meta" in newvalue) {
				if("rel" in newvalue.meta) {
					$scope.template = "app/" + newvalue.meta.rel + ".html";
					$scope.$bookmark(newvalue);
				}
			}
		}
	});
	$scope.$watch('mode', function(newvalue, oldvalue) {
		$scope.$bookmark($scope.resource);
	});

	$scope.resource = graph;
	$scope.form = {};
	$scope.mode = MODE_SHOW;
	
	$scope.$bookmark = function(rel) {
		var q = {};
		q.b = rel.meta.rel;
		if(angular.isDefined($scope.mode)) {
			q.m = $scope.mode;
		}
		$location.search(q);
	};
	
	$scope.parents = function() {
		var res = [];
		var curr = $scope.resource;
		while(angular.isDefined(curr)) {
			if(curr.canGet()) {
				res.push(curr);
			}
			curr = curr.parent;
		}
		return res.reverse();
	};
	
	$scope.isList = function() {
		return angular.isArray($scope.resource.data);
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
		$scope.resource = link;
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
			$scope.formval = {};
			$scope.form = {};
		};
		var error = function(res) {
			$scope.formval = res.data;
		};
		if($scope.mode === MODE_EDIT) {
			$scope.resource.update($scope.form).then(update, error);
		}
		else if($scope.mode === MODE_CREATE) {
			$scope.resource.create($scope.form).then(update, error);
		}
	};

	$scope.edit = function() {
		$scope.mode = MODE_EDIT;
		$scope.form = $scope.resource.data;
	};

	$scope.remove = function() {
		$scope.resource.remove().then(function(resp) {
			$scope.mode = MODE_SHOW;
			$scope.form = {};
			$scope.resource = $scope.resource.parent.parent; // TODO: cheating, parent == the collection
		});
	};
}

MainCtrl.resolve = {
	graph: function(RestGraph, $location) {
		return RestGraph("api").init().then(function(d){
			return d.get();
		});
	}
};

angular.module('geekseek', ['restgraph','$strap.directives'], function() {
}).config(function($routeProvider, $httpProvider, $locationProvider, RestGraphProvider){
	
	$routeProvider.otherwise({
		templateUrl: 'app/front.html',
		controller: MainCtrl,
		resolve: MainCtrl.resolve,
		reloadOnSearch: false
	});
	
	$locationProvider.html5Mode(false);
	$locationProvider.hashPrefix = '!';

	$httpProvider.defaults.headers.common["Accept"] = "application/vnd.ced+json"; 
	$httpProvider.defaults.headers.post["Content-Type"] = "application/vnd.ced+json";
	$httpProvider.defaults.headers.put["Content-Type"] = "application/vnd.ced+json";

});