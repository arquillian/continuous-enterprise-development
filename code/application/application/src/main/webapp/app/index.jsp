<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>Continuous Enterprise Development</title>
    <base href="<%=request.getAttribute("BASE_ROOT")%>" />
    <meta name="fragment" content="!" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <style type="text/css">
      /*<![CDATA[*/
        body {
          padding-top: 70px;
          padding-bottom: 70px;
        }
      /*]]>*/
    </style>
    <link href="webjars/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="webjars/bootstrap-datetimepicker/6aa746736d/css/datetimepicker.css" rel="stylesheet" type="text/css">
    <link href="webjars/font-awesome/3.2.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    
    <link href="styles/base.css" rel="stylesheet" type="text/css" />

    <!--[if lt IE 9]>
      <script src='//html5shim.googlecode.com/svn/trunk/html5.js' type='text/javascript'></script>
    <![endif]-->
  </head>
  <body data-ng-app="geekseek">

    <!-- Navigation -->

    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
       <div class="container">
          <a class="navbar-brand" href="#"><img src="images/logo.png" height="25" width="25"/></a>
          <a href="" class="navbar-brand">GeekSeek</a>

          <ul class="nav navbar-nav">
            <li>
             <form class="navbar-form navbar-left" role="search">
               <input type="text" name="search" class="form-control" style="width:200px;"/>
               <i class="icon-search icon-white"></i>
             </form>
          </li>
         </ul>

         <ul class="nav navbar-nav pull-right" data-ng-cloak data-ng-controller="UserCtrl" data-ng-show="current.knownstate">
	        <li class="user" data-ng-show="current.authenticated">
                <a data-ng-click="view()"><img data-ng-src="{{current.user.data.avatarUrl}}" /> {{current.user.data.name}}</a>
            </li>
            <li class="user" data-ng-hide="current.authenticated">
               <a href="../auth" class="btn">Login</a>
            </li>
	     </ul>
       </div>
    </nav>
    <div class="container content" data-ng-include="'front.html'" data-ng-controller="MainCtrl">
      loading..
    </div>

  </body>
  <script src="webjars/jquery/2.0.3/jquery.min.js" type="text/javascript"></script>
  <script src="webjars/angularjs/1.2.0rc1/angular.js" type="text/javascript"></script>
  <script src="webjars/angularjs/1.2.0rc1/angular-route.js" type="text/javascript"></script>
  <script src="webjars/angularjs/1.2.0rc1/angular-sanitize.js" type="text/javascript"></script>
  <script src="webjars/angular-ui-utils/47ff7ef35c/ui-utils.min.js" type="text/javascript"></script>
  <script src="webjars/bootstrap/3.0.0/js/bootstrap.min.js" type="text/javascript"></script>
  <script src="webjars/bootstrap-datetimepicker/6aa746736d/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
  <script src="webjars/core/graph.js" type="text/javascript"></script>
  <script src="webjars/core/geekseek.js" type="text/javascript"></script>

</html>