export function getSocailInfo (type) {
  switch (type) {
    case 'GITHUB':
      return { logo: '', name: 'GitHub', url: 'https://github.com' }
    case 'WEIBO':
      return { logo: '', name: '微博', url: 'https://github.com' }
    case 'GITEE':
      return { logo: '', name: 'Gitee', url: 'https://github.com' }
    case 'DINGTALK':
      return { logo: '', name: '钉钉', url: 'https://github.com' }
    case 'BAIDU':
      return { logo: '', name: '百度', url: 'https://github.com' }
    case 'CSDN':
      return { logo: '', name: 'CSDN', url: 'https://github.com' }
    case 'CODING':
      return { logo: '', name: 'Coding', url: 'https://coding.net' }
    case 'TENCENT_CLOUD':
      return { logo: '', name: '腾讯云', url: 'https://coding.net' }
    case 'OSCHINA':
      return { logo: '', name: '开源中国', url: 'https://coding.net' }
    case 'ALIPAY':
      return { logo: '', name: '支付宝', url: 'https://coding.net' }
    case 'QQ':
      return { logo: '', name: 'QQ', url: 'https://coding.net' }
    case 'WECHAT':
      return { logo: '', name: '微信', url: 'https://coding.net' }
    case 'TAOBAO':
      return { logo: '', name: '淘宝', url: 'https://coding.net' }
    case 'GOOGLE':
      return { logo: '', name: '谷歌', url: 'https://coding.net' }
    case 'FACEBOOK':
      return { logo: '', name: 'FaceBook', url: 'https://coding.net' }
    case 'DOUYIN':
      return { logo: '', name: '抖音', url: 'https://coding.net' }
    case 'LINKEDIN':
      return { logo: '', name: '领英', url: 'https://coding.net' }
    case 'MICROSOFT':
      return { logo: '', name: '微软', url: 'https://coding.net' }
    case 'MI':
      return { logo: '', name: '小米', url: 'https://coding.net' }
    case 'TOUTIAO':
      return { logo: '', name: '头条', url: 'https://coding.net' }
    case 'TEAMBITION':
      return { logo: '', name: 'Teambition', url: 'https://coding.net' }
    case 'RENREN':
      return { logo: '', name: '人人', url: 'https://coding.net' }
    case 'PINTEREST':
      return { logo: '', name: 'Pinterest', url: 'https://coding.net' }
    case 'STACK_OVERFLOW':
      return { name: 'StackOverFlow', url: 'https://coding.net' }
    case 'HUAWEI':
      return { name: '华为', url: 'https://coding.net' }
    case 'WECHAT_ENTERPRISE':
      return { logo: '', name: '企业微信', url: 'https://coding.net' }
    case 'GITLAB':
      return { logo: '', name: 'Gitlab', url: 'https://coding.net' }
    case 'KUJIALE':
      return { logo: '', name: '酷家乐', url: 'https://coding.net' }
    default:
      throw new Error('无法获取社交帐号信息')
  }
}
