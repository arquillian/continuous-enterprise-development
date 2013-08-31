
var optionsInit = {
    setup : function() {
        var injector = angular.injector([ 'ng', 'restgraph', 'ngMock' ]);
        this.$scope = injector.get('$rootScope').$new();
        this.$http = injector.get('$httpBackend');
        this.$graph = injector.get('RestGraph');
        this.$requestURL = "http://geekseek.continuousdev.org/api/"
        this.$initGraph = function(allow, test) {
            this.$http.when('OPTIONS', this.$requestURL).respond({}, {'Allow': allow})
            this.$graph(this.$requestURL).init().then(
                    function(node) {
                        test(node)
                        start()
                    })
             this.$http.flush();
        }
    },
    teardown: function() {
        this.$http.verifyNoOutstandingExpectation();
        this.$http.verifyNoOutstandingRequest();
        this.$http.resetExpectations();
    }
};

var getInit = {
        setup : function() {
            var injector = angular.injector([ 'ng', 'restgraph', 'ngMock' ]);
            this.$scope = injector.get('$rootScope').$new();
            this.$http = injector.get('$httpBackend');
            this.$graph = injector.get('RestGraph');
            this.$requestURL = "http://geekseek.continuousdev.org/api/"
        },
        teardown: function() {
            this.$http.verifyNoOutstandingExpectation();
            this.$http.verifyNoOutstandingRequest();
            this.$http.resetExpectations();
        }
    };

module("Service OPTIONS", optionsInit)
asyncTest("can get?", 1, function() {
    this.$initGraph('GET', function(node) {
        ok(node.canGet(), "Should be able to create Resource")
    })
});
asyncTest("can remove?", 1, function() {
    this.$initGraph('DELETE', function(node) {
        ok(node.canRemove(), "Should be able to remove Resource")
    })
});
asyncTest("can create?", 1, function() {
    this.$initGraph('POST', function(node) {
        ok(node.canCreate(), "Should be able to create Resource")
    })
});
asyncTest("can update (patch)?", 1, function() {
    this.$initGraph('PATCH', function(node) {
        ok(node.canUpdate(), "Should be able to create Resource")
    })
});
asyncTest("can update (put)?", 1, function() {
    this.$initGraph('PUT', function(node) {
        ok(node.canUpdate(), "Should be able to update Resource")
    })
});

module("Service Get", getInit)
asyncTest("discover children", 2, function() {
    var linkURL = this.$requestURL + '/a';
    this.$http.when('OPTIONS', this.$requestURL).respond({}, {'Allow':'GET'})
    this.$http.when('GET', this.$requestURL).respond({name:'test', link : [{href:linkURL, rel:'test', mediaType:'test'}]}, {})
    this.$http.when('OPTIONS', linkURL).respond({}, {'Allow':'GET'})

    this.$graph(this.$requestURL).init().then(function(node) {
        node.get().then(function(node) {
            node.links.then(function(links) {
                ok(links.length == 1, "should have found one link")
                ok(links[0].meta.rel === 'test', "should have found one link")
                start()
            })
        })
    })
    this.$http.flush();
})
asyncTest("handle arrays as nodes", 3, function() {
    var linkURLOne = this.$requestURL + 'one';
    var linkURLTwo = this.$requestURL + 'two';
    this.$http.when('OPTIONS', this.$requestURL).respond({}, {'Allow':'GET'})
    this.$http.when('GET', this.$requestURL).respond([
          {name:'One', link:[{href:linkURLOne, rel:'self', mediaType:'test'}]},
          {name:'Two', link:[{href:linkURLTwo, rel:'self', mediaType:'test'}]}
    ], {});
    this.$http.when('OPTIONS', linkURLOne).respond({}, {'Allow':'GET'})
    this.$http.when('OPTIONS', linkURLTwo).respond({}, {'Allow':'GET'})

    this.$graph(this.$requestURL).init().then(function(node) {
        node.get().then(function(node) {
            ok(node.isArray())
            node.data.then(function(data) {
                ok(data[0].canGet(), "Sub node should be gettable");
                ok(data[1].canGet(), "Sub node should be gettable");
                start();
            })
        })
    })
    this.$http.flush();
})