<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>正在跳转到支付宝支付...</title>
</head>
<body>
    <div style="text-align: center; margin-top: 50px;">
        <h2>正在跳转到支付宝支付页面，请稍候...</h2>
        <div style="margin-top: 30px;">
            <img src="https://img.alicdn.com/imgextra/i3/O1CN01YHf1uW1mKf2wX6J4a_!!6000000004732-2-tps-200-200.png" alt="支付宝" width="100" height="100">
        </div>
        <div style="margin-top: 30px;">
            <p>如果您的浏览器没有自动跳转，请点击下方按钮</p>
            <form id="alipayForm" method="post" action="https://openapi-sandbox.dl.alipaydev.com/gateway.do">
                ${alipayForm}
                <input type="submit" value="前往支付宝支付" style="padding: 10px 20px; font-size: 16px; background-color: #1677FF; color: white; border: none; border-radius: 4px; cursor: pointer;">
            </form>
        </div>
    </div>

    <script type="text/javascript">
        // 自动提交表单
        document.getElementById('alipayForm').submit();
    </script>
</body>
</html>