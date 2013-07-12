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
          padding-top: 60px;
          padding-bottom: 60px;
        }
      /*]]>*/
    </style>
	<link href="//netdna.bootstrapcdn.com/bootswatch/2.3.1/slate/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/3.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    
    <link href="styles/base.css" rel="stylesheet" type="text/css" />

    <!--[if lt IE 9]>
      <script src='//html5shim.googlecode.com/svn/trunk/html5.js' type='text/javascript'></script>
    <![endif]-->
  </head>
  <body data-ng-app="geekseek">

    <!-- Navigation -->

    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          
          <a class="brand" href="#"><img src="images/logo.png" height="25" width="25"/></a>
          
          <ul class="nav span4">
            <li>
              <a href="">GeekSeek</a>
            </li>
          </ul>

          <form class="navbar-search pull-left">
            <input type="text" name="search" class="search-query"/>
            <i class="icon-search icon-white"></i>
          </form>

          <ul class="nav pull-right">
            <li class="user">
                <a href="my"><img src="http://www.gravatar.com/avatar/3f27861ec08730fd02c91fe4129d2668?s=24" /> Aslak Knutsen</a>
            </li>
            <li>
              <a href=""><i class="icon-align-justify icon-white"></i></a>
            </li>    
          </ul>
        </div>
      </div>
    </div>

    <div class="content" data-ng-view="">


    </div>

  </body>
  <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.1/jquery.js" type="text/javascript"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.1.5/angular.js" type="text/javascript"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/2.3.2/js/bootstrap.js" type="text/javascript"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.0.2/js/bootstrap-datepicker.js" type="text/javascript"></script>
  <script src="scripts/bootstrap-timepicker.min.js" type="text/javascript"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/angular-strap/0.7.4/angular-strap.js" type="text/javascript"></script>
  <script src="scripts/graph.js" type="text/javascript"></script>
  <script src="scripts/geekseek.js" type="text/javascript"></script>

</html>