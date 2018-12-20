<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>上传单个文件示例</title>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main.css" type="text/css" />
</head>
<body>
<div align="center">

    <h1>上传附件</h1>
    <form method="post" action="/project/upload" enctype="multipart/form-data">
        <input type="file" name="file"/>
        <input type="text" name="devTypeID">
        <button type="submit" >提交</button>
    </form>
    <h1>下载文件</h1>
    <a href="/project/down" download="直流电阻模板.xlsx">点击下载</a>
</div>
</body>
</html>