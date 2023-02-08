<html>
<body>
<h2>Hello World!</h2>

springmvc File Upload
<form name = "form1" action="/manage/product/upload.do" method = "post" enctype="multipart/form-data">
    <input type="file" name = "upload_file"/>
    <input type="submit" value = "springmvc File Upload"/>
</form>

richtext File Upload
<form name = "form1" action="/manage/product/richtext_img_upload.do" method = "post" enctype="multipart/form-data">
    <input type="file" name = "upload_file"/>
    <input type="submit" value = "richtext File Upload"/>
</form>

</body>
</html>
