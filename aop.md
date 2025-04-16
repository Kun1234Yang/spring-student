✅ 推荐源码入口类（重要）
类名	作用
AnnotationAwareAspectJAutoProxyCreator	AOP 创建器
AbstractAutoProxyCreator	自动代理抽象类
ProxyFactory	创建代理
JdkDynamicAopProxy	JDK 动态代理实现
CglibAopProxy	CGLIB 动态代理实现
AspectJAdvisorFactory	将 @Aspect 注解的方法封装成 Advisor
ReflectiveMethodInvocation	执行拦截器链

🔄 方法调用流程图（调试顺序）
less
复制
编辑
调用 OrderService.createOrder()
↓
Spring 容器已注入代理对象（不是原始类）
↓
进入代理对象的 invoke() / intercept()
↓
获取 Advice 列表（通知）
↓
封装为 ReflectiveMethodInvocation
↓
调用 proceed()：
├─ 前置通知 (@Before)
├─ 环绕通知 (@Around before)
├─ 目标方法（原方法）
├─ 环绕通知 (@Around after)
└─ 后置通知 (@After, @AfterReturning, @AfterThrowing)

📊 Bean 创建流程总览图
scss
复制
编辑
SpringBootApplication.run()
↓
刷新容器 refresh()
↓
调用 BeanFactory 的 finishBeanFactoryInitialization()
↓
初始化所有非懒加载单例 Bean
↓
createBean(beanName)
├─ 实例化（Constructor）
├─ 属性填充（@Autowired）
├─ 初始化（@PostConstruct）
└─ BeanPostProcessor 前后处理（如 AOP、事务）

🧪 一个完整创建链条的例子：OrderService 创建过程
ruby
复制
编辑
createBean("orderService")
├─ 实例化 OrderService
├─ 填充 @Autowired 属性
├─ 调用 @PostConstruct
└─ applyBeanPostProcessorsAfterInitialization()
└─ AnnotationAwareAspectJAutoProxyCreator
└─ 判断匹配切面
└─ 创建代理 OrderService$$EnhancerBySpringCGLIB

🧭 Spring 启动 + AOP 织入全流程图（文字版结构图）
text
复制
编辑
SpringApplication.run()
│
├──> AbstractApplicationContext.refresh()
│     │
│     └──> invokeBeanFactoryPostProcessors()    ← 处理配置类、@Bean 等
│
├──> registerBeanPostProcessors()               ← 注册 AOP 相关 BPP
│     └── AnnotationAwareAspectJAutoProxyCreator
│           ↑ 实现了 BeanPostProcessor
│
└──> finishBeanFactoryInitialization()
└──> preInstantiateSingletons()
└──> getBean("orderService")        ← 开始创建目标 Bean
└──> createBean()
└──> doCreateBean()
├──> createBeanInstance()       ← new 实例
├──> populateBean()             ← 属性注入
└──> initializeBean()
├── applyBPP.beforeInit()
├── invokeInitMethods()   ← @PostConstruct
└── applyBPP.afterInit()
└── postProcessAfterInitialization()
└── wrapIfNecessary()
├── isInfrastructureClass() ❌
├── shouldSkip() ❌
├── findCandidateAdvisors()
└── createProxy()
├── JdkDynamicAopProxy
└── CglibAopProxy
