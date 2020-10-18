**为了更好的解决你的问题, 建议在发表前, 先浏览以下内容。**

### issue
- 提供你的系统信息(发行版名字，版本号，浏览器等)。
- 尽量详细描述你的问题，以及这个问题是如何出现的，最好能把 操作步骤大概说一下，以便我能重现这个问题。可以通过截图，录屏，浏览器Console报错等方式提交更多的信息。
- 超过 7 天未对 Contributors 的回复进行反馈的，默认将认为已解决关闭 Issue
- 请使用提供的 Issue 模板格式发布
### pull request
- 请不要修改项目的换行规格（重要）
- 提交代码的基本格式请遵守 .editorconfig 和 eslint （提交前请用 yarn run lint --no-fix检查是否有 rules 未通过）如果未遵守，将直接关闭 pr
- 配置 git config `git config core.autocrlf input`, 具体命令
```
git config --global core.autocrlf input
```
- 提交的 pull request 请描述清楚做了什么改动
- 尽量遵循本项目 commit message 如下：
    - 修复 bug ： fix: bug title (如果有 issue，请在结尾带 #issue 号)
    - 修复包管理信息: fix(package): move polyfills dependencie to dev
    - 新增功能： feat: add multi-tabs
    - 更新文档： docs: xxxx
    - 其他操作： chore: rename loadding -> loading
    - 重构代码： refactor: multi-tabs

- 不合并：自有业务逻辑、无意义代码、格式化代码
### 最后
复制大家都在说的话：

> 最最重要的一点, 如果有什么问题, 请尽量把语气放平和一点, 不要像某社区里的用户那样, 一副天下人都欠你的语气来提问题。

> 项目维护者本身也有自己的工作，开源项目开发也只是个人兴趣，也是方便自己使用，本项目最初就是从生产环境剥离出来的骨框架。
所以，如果你觉得该项目内有什么是你不需要的，有一些功能你不想保留的话，而你又有一定的开发经验的话, 可以自己 fork 一份，自己去移除掉部分功能模块，如果有不懂的地 方, 可以留言或邮件联系我，在我有空的时候可以适当解答一下你的疑惑（但不是有义务必须解答，你要理解）。

另外 合理的、多数人都需要的功能、建议，我会考虑。但是精力有限, 不一定会马上加入。