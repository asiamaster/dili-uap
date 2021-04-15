# 1. 使用uap-oauth-sdk提供的JustAuthController登录，获取token信息
url:http://uap.diligrp.com/api/oauth/render/uap

# 2. 刷新token
OAuthClientController提供刷新token的接口
url:http://uap.diligrp.com/api/oauthclient/refresh/uap/{uuid}
uuid为登录用户id

OAuthClientController继承JustAuthController
- getRefreshTokenByUuid方法:
子类实现，用于刷新token时根据用户id获取refreshToken，如果用户不存在，需要抛出AuthException异常

- updateRefreshToken方法:
子类实现(可选)
在调用refreshAuth方法后，(在数据库或缓存中)更新用户的refreshToken