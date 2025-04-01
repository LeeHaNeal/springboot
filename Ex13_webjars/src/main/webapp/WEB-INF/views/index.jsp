<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Fade In/Out Example</title>
<link href="/webjars/bootstrap/5.3.3/css/bootstrap.min.css" rel="stylesheet">
<script src="/webjars/jquery/3.7.1/jquery.min.js"></script>
<script src="/webjars/bootstrap/5.3.3/js/bootstrap.min.js"></script>
</head>
<body>
    <button type="button" class="btn btn-outline-warning" onclick="test1();">fadeOut</button>
    <button type="button" class="btn btn-outline-info" onclick="test2();">fadeIn</button>
    
    <img id="img1" src="/img/img1.jpg" style="width: 300px; height: auto;">
    
    <script type="text/javascript">
        function test1() {
            $("#img1").fadeOut();
        }
        
        function test2() {
            $("#img1").fadeIn();
        }
    </script>
</body>
</html>
