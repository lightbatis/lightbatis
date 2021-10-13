## 错误的处理
unable to access 'https://github.com/lightbatis/lightbatis.git/': LibreSSL SSL_connect: SSL_ERROR_SYSCALL in connection to github.com:443

方法1.打开终端在终端中执行以下命令就可以了。
```shell
git config --global --add remote.origin.proxy ""
```