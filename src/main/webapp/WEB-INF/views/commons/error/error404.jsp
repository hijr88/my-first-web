<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>존재하지 않는 페이지</title>

	<%-- 내가만든 스타일--%>
	<%--  <link rel="shortcut icon" href="./favicon.ico"> 
    <link rel="icon" href="./favicon.ico">
    <link rel="stylesheet" href="../css/error.css"> --%>
    
    <link rel="shortcut icon" href="<c:url value="/resources/images/favicon.ico" />">
    <link rel="icon" href="<c:url value="/resources/images/favicon.ico" />">
	<link rel="stylesheet" href="<c:url value="/resources/css/error.css" />">
</head>

<body>
    <div class="over-lay">
        <div class="content">
            <div class="img">
                <img class="" src="<c:url value="/resources/images/page_not_found.jpg"/>" alt="page_not_found">
            </div>
            <h2>요청하신 페이지가 존재하지 않습니다.</h2>
            <a href="${contextPath}/index" class="back-home">돌아가기</a>
        </div>
    </div>
</body>
</html>