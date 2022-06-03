# Getting Started


一、**配置文件说明：**

    1.系统限流策略 配置文件 ratelimitStrategy.properties
        a.计数器 (实现)使用方式：注解 在方法 @CounterRateLimitAnnocation(timeLimit = 1, numLimit = 100)---timelimt：时间范围 单位(分钟)  numLimit:数量限制
        b.滑动窗口（预留扩展）
        c.漏斗桶 （预留扩展）
        d.令牌桶（预留扩展）
    2.分配快递策略 配置文件 dispatchCourierStrategy.properties
        a.MATCH(实现)
   	    b.FIFO(实现)
   	    c.LRU（预留扩展）
    3.系统实时策略 配置文件 realTimeConsumeQueueStrategy.properties
   	    a.PSEUDO_REALTIME (实现) （pseudo_realtime.schedule.period--定时消费频率，如果定义为0，则和REAL_TIME一样的效果）
   	    b.REAL_TIME（实现）
    	c.SOCKET_REALTIME（预留扩展)
    4.订单配置 ：orderTest.properties --配置模拟生成订单数量
二、**系统前置条件**

    1.餐厅唯一：为了保证保证单例性（直接用枚举的写法）

    2.核心队列
        订单队列:          orderQueue(LinkedBlockingQueue)
        餐厅接单队列:      receiveQueue (LinkedBlockingQueue<Order> )
        餐厅制作完成队列:   readyQueue(DelayQueue<ReadyDTO>);
        快递员到达队列:     courierArrivedPriorityQueue(PriorityQueue<CourierArriveDTO>)
        快递员延迟队列：    courierdelayQueue（DelayQueue<CourierDelayDTO>）

三、**概要设计+详细设计**

    基于UML做系统设计
    1.用例图
    2.时序图
    3.流程图
    具体文件见系统中UML design文件夹
![img.png](img.png)
四、**系统设计**

    1.思考：
        因为我是业务导向型系统，需要考虑下面的问题：
            1.业务有足够的了解，
            2.能够满足当下以及未来可能要支持的业务需求（扩展）
        基于此我们要遵循SOLID设计原则，设计出灵活，解耦、侵入性小、可扩展、可快速迭代的系统
        同时也利于单元测试！
    2.方法论：
        **遵循SOLID**  
        Single Responsibility Principle-----（SRP）单一职责原则
        Open Close Principle-----------------(OCP)开闭原则（扩展开放，修改关闭。）
        Liskov Substitution Principle--------(LSP)里式替换原则
        Interface segregation Principle------(ISP)接口隔离原则
        Dependency injection Principle-------(DIP)控制反转
    
    3.实操：
        1.单例模式（enum包）：为了把数据全局写入同一份内存（队列+链表+HashMap），使用了单例，保证了系统中唯一性
        2.策略模式（strategy包)：基于接口编程，对系统功能做抽象，提取出公共部分，然后做不同的实现，这里为了系统扩展开放做准备
            所有策略都放在map中，基于查表法来调用
            代码最小改动每次新增策略实现，也无需改动代码put到map中（基于spring SPI机制）
         a.快递员的调度策略
            a.Match
            b.FIFO
         b.hash算法策略实现（调度策略中Match策略给订单分配快递员） --预留多种实现方式
         c.限流策略--预留多种实现方式
            a.计数器限流
            b.滑动窗口限流--sentinal之类
            c.漏斗桶限流
            d.令牌桶限流
        3.监听者模式（event+listener包）---异步解耦。基于Event机制（spring的，也可以基于google的Event-Bus框架）
            a.下单成功监听事件
            b.餐厅接收到订单事件
            c.餐厅制作完订单事件
        4.桥接模式（bridge包）---异常订单告警
            异常等级 抽象接口
            告警方式 抽象接口
            二者分别做各自的多种实现
            最后通过组合的方式，拼在一起
            实现了异常登录和告警方式的灵活排列组合
        5.代理模式（aop包）--开发了自定义注解(底层基于java反射)
            a.日志打印
            b.限流功能


五、**并发**

    1.做了限流控制
    2.ConcurrentHashMap--加锁map
    3.synchronized --加锁
    4.AtomicInter 原子类 保证id生成的唯一性
    5.Volital --可见性 保证线程安全 
    6.队列用到LinkedBlockingQueue 基于Volital和CAS 自旋 保证线程安全
    7.PriorityBlockingQueue  ----也是线程安全的
    8.DelayQueue 由于其堆的数据结构存储，不会出现多个线程操作一个数据，所有本身也是线性安全的
    9.单例加锁检查
    10.单例（本身的唯一性避免了线程安全问题）

六、**数据结构**

    1.数组
    2.队列:
        a.阻塞队列 LinkedBlockQueue(多线程环境，通过队列实现数据共享。利用队列来传递数据)
                注意点：
                可能问题：生产速度>消费速度， 又没有容量限制， 所以会一直膨胀 数据大量堆积----->导致内存耗尽，OOM
                解决方案：通过构造函数确定队列大小，避免上面情况发送，
                )
        b.优先级队列：priorityQueue 用来保存快递的到达顺序（重新了compare方法，比较的是他们的到达时间）
        c.延迟队列：delayQueue  快递到店取餐，发现餐还没有准备好，则进入延时队列，等延时的时间到了，重新唤起去取餐
    3.hash:HashMap
    4.线程池：SchedualThreadPoolExecutor

七、**单元测试**

    基于junit
    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = OrderDeliveryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    或者也可基于mojito 来mock数据
    测试用例可以全部pass

八、**文档化**

    基于swagger输出

