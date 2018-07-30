# faceLoginByBaiDu
对百度人脸识别api进行修改和包装，使用起来更加方便，并带有人脸识别注册登录和更新实例

LoginAction.java   
getAuth   有获取access_token

insertFace 添加人脸和更新人脸，当通过userId得到人脸则说明已经注册了需要更新（我设定每个人只存入了一个人脸）

登录（查询人脸） ，通过userId来进行查询，我这里设userId为电话号码。


WebFace
封装了所有关于人脸的操作，只需要传入特点的参数即可。

每个方法的上面是返回的信息。

HttpUtil
本类和百度api的基本相同。