/**
 * 
 */
(function(){

var module = angular.module('restgraph', []);

module.provider('RestGraph', function() {
	var discoverChildren = function(context) {
		var children = [];
		
		if("link" in context.data) {
			for(var i = 0; i < context.data.link.length; i++) {
				var link =  context.data.link[i];
				var d = context.$q.defer();
				children.push(d.promise);
				
				var linkNode = new Node(context.$q, context.$http, link, link.href, context.parent);
				linkNode.init().then(function(node) {
					d.resolve(linkNode);
				});
			};
			context.data.$link = context.data.link;
			delete context.data.link;
		}
		return context.$q.all(children);
	};
	var Node = function($q, $http, meta, startURL, parent) {
		this.$q = $q,
		this.$http = $http,
		this._meta = meta,
		this._startURL = startURL,
		this._parent = parent,
		this._data = {},
		this._options = [],
		this._children = [];
	};
	
	Node.prototype.__defineGetter__("parent", function(){
        return this._parent;
    });
	Node.prototype.__defineGetter__("data", function(){
        return this._data;
    });
	Node.prototype.__defineGetter__("links", function(){
        return this._children;
    });
	Node.prototype.__defineGetter__("meta", function(){
        return this._meta;
    });
	Node.prototype.canGet = function() {
		return this._options.indexOf('GET') > -1;
	};
	Node.prototype.canRemove = function() {
		return this._options.indexOf('DELETE') > -1;
	};
	Node.prototype.canUpdate = function() {
		return this._options.indexOf('PUT') > -1;
	};
	Node.prototype.canCreate = function() {
		return this._options.indexOf('POST') > -1;
	};
	Node.prototype.init = function() {
		var self = this;
		var d = this.$q.defer();
		this.$http({method: 'OPTIONS', url: this._startURL})
		.success(function(data, status, headers, config) {
			self._options = headers("Allow").split(", ");
			d.resolve(self);
		}).error(function(data, status, headers, config) {
			d.reject("failed");
		});
		return d.promise;
	};
	
	Node.prototype.get = function() {
		var self = this;
		var d = this.$q.defer();
		this.$http.get(this._startURL).then(function(data) {
			self._data = data.data;
			self._children = discoverChildren({data: self._data, parent:self, $q: self.$q, $http: self.$http});  
			d.resolve(self);
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
		return d.promdise;
	};
	Node.prototype.remove = function() {
		var self = this;
		var d = this.$q.defer();
		this.$http.delete(this._startURL).then(function(data) {
			self._data = {}
			d.resolve(self.parent);
		});
		return d.promise;
	};
	Node.prototype.create = function(updateddata) {
		var self = this;
		var d = this.$q.defer();
		this.$http.post(this._startURL, updateddata).then(function(data) {
			var node = new Node(self.$q, self.$http, self.meta, data.headers('Location'), self);
			node.init().then(function(res) {
				d.resolve(res);
			});
		}, function(data) {
			self._data=data.data;
			d.reject(self);
		});
		return d.promise;
	};
	
	this._initURL;
	this.setInitURL = function(url) {
		this._initURL = url;
	}
	
	self = this;
	this.$get = function($q, $http) {
		return new Node($q, $http, {href:self._initURL, rel: 'root'}, self._initURL);
	};

});

})();