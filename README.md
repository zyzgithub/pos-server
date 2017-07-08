### Build Project
#### 构建WEB以及scheduler的测试以及生产WAR包/ZIP包
    WEB位于pos-web/target目录，pos-ctc-dev.war为测试包，pos-ctc-prod.war为生产包
    SCHEDULER位于pos-scheduler/target目录，pos-scheduler-dev.zip为测试包，pos-scheduler-prod.zip为生产包
#### 构建命令
    mvn clean install -P deploy
#### 模块释义
    根据业务需求，将代码摆放到对应模块中，模块间引用基础服务，不能直接使用引用模块Mapper或者Jpa内方法
    基础模块引用关系：pos-common->pos-core->pos-base,即：新建的模块直接引用pos-common即可
    pos-base:           基础模块，基础服务，不含业务
    pos-casher:         金融业务模块
    pos-common:         插件/工具模块，不含业务
    pos-core:           核心模块，核心服务，不含业务
    pos-extended:       扩展模块、增值服务等
    pos-item:           商品模块，商品查询，入库，上下架等
    pos-maintenance:    运行维护模块，提供后台维护业务操作入口
    pos-order:          订单模块，订单查询，下单等
    pos-passport:       用户模块，包含用户登录，角色权限控制等
    pos-payment:        支付模块，包含支付宝微信条码支付，二维码支付，余额支付等
    pos-purchase:       采购模块，包含一键采购商品计算，一键采购下单等
    pos-qrcode:         二维码扩展模块，二维码扫码以及生成等业务操作
    pos-report-form:    报表模块，任何报表统计等操作均应放在此模块下
    pos-scheduler:      定时器应用，定时相关业务任务模块，任何定时操作均需摆放此模块下
    pos-settlement:     结算中心模块，包含结算计算以及结算支付等操作
    pos-supply-chain:   供应链模块，目前涉及供应链相关的业务均放在此模块下
    pos-web:            WEB应用，不含业务，包含thymeleaf模板页面等静态资源文件以及公共配置
    