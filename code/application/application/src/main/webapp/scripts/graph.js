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

				var resolve = function(defer) {
					return function(node) {
						defer.resolve(node);
					};
				};
				var linkNode = new Node(context.$q, context.$http, link, link.href, context.parent);
				linkNode.init().then(resolve(d));
			};
			context.data.$link = context.data.link;
			delete context.data.link;
		}
		all = context.$q.all(children);
		return all;
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
	Node.prototype.__defineGetter__("root", function(){
        var par = this.parent;
        if(par == null) {
            return this;
        }
        while(angular.isDefined(par.parent)) {
            par = par.parent;
        }
        return par;
    });
	Node.prototype.__defineGetter__("url", function(){
        return this._startURL;
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
		return this._options.indexOf('PUT') > -1 || this._options.indexOf('PATCH') > -1;
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
			d.reject(self);
		});
		return d.promise;
	};
	
	Node.prototype.getLink = function(name) {
		for(var i = 0; i < this.links.length; i++) {
			var link = this.links[i];
			if(link.rel === name) {
				return link;
			}
		}
		return;
	};

	Node.prototype.bookmark = function(type, id) {
		var self = this;
		var root = this.root;
		//var link = root.getLink("bookmark"); // TODO: lookup bookmark template url from root

		var d = this.$q.defer();
		var url = root.url + "/bookmark/" + type + "/" + id;
		this.$http.get(url).then(function(data) {
			var linkNode = new Node(
					self.$q, self.$http,
					{rel: type, url: data.config.url, mediaType: data.headers('Content-Type')}, url, root);
			linkNode.init().then(function(node) {
				node.get().then(function(node) {
					d.resolve(node);
				});
			});
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
		}, function(data){
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
	
	this.$get = function($q, $http) {
		return function(baseURL) {
			return new Node($q, $http, {href:baseURL, rel: 'root'}, baseURL);
		};
	};

});

})();