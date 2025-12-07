<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>正在跳转到支付宝支付...</title>
    </head>
    <body>
        <div style="text-align: center; margin-top: 50px">
            <h2>正在跳转到支付宝支付页面，请稍候...</h2>
            <div style="margin-top: 30px">
                <img
                    src="https://img.alicdn.com/imgextra/i3/O1CN01YHf1uW1mKf2wX6J4a_!!6000000004732-2-tps-200-200.png"
                    alt="支付宝"
                    width="100"
                    height="100"
                />
            </div>
            <div style="margin-top: 30px">
                <p>如果您的浏览器没有自动跳转，请点击下方按钮</p>
                <a
                    id="alipayLink"
                    href="${alipayForm}"
                    style="
                        display: inline-block;
                        padding: 10px 20px;
                        font-size: 16px;
                        background-color: #1677ff;
                        color: white;
                        border: none;
                        border-radius: 4px;
                        cursor: pointer;
                        text-decoration: none;
                    "
                    >前往支付宝支付</a
                >
            </div>
        </div>

        <script type="text/javascript">
            // 自动跳转到支付宝URL
            window.location.href = document.getElementById("alipayLink").href;
        </script>
    </body>
</html>
