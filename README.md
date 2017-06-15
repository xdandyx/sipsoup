#sipsoup

sipsoup是一个基于Jsoup的xpath实现，他将Jsoup的cssQuery变成了xpath语法的一部分，可以实现在xpath内部执行cssQuery和xpath混合模式的链式文档查询

是一款纯Java开发的使用xpath解析html的解析器，xpath语法分析与执行完全独立，html的DOM树生成借助Jsoup。

sipSoup本身也应该叫做Xsoup,JsoupXpath之类的,但是他出身太晚了,名字被占了,但是我觉得还是应该喝汤,所以我喜欢sipSoup这个名字。

每一个爬虫框架作者都应该实现一个xpath, sipsoup 出现也是如此,在我设计爬虫框架[vscrawler](http://git.oschina.net/virjar/vscrawler)的时候,考虑如何定位抽取数据,甚至如何整合其他爬虫框架的抽取API,
然后调研了Xsoup和JsoupXpath,看到JsoupXpath眼前一亮,我觉得他肯定适合我的需要,所以尝试使用JsoupXpath对接了Xsoup,因为JsoupXpath实现了xpath2.0的绝大部分语法功能,
使用JsoupXpath可以非常灵活自由的实现xpath。但是在整合的过程,我发现JsoupXpath有一点不符合我的需求,就是他都是运行的适合抛错,我希望能够在编译xpath表达式的时候,就可以做语法
检查,我希望JsoupXpath可以类似Xsoup一样做链式抽取,我希望规则模型可以缓存。所以尝试着对JsopXpath进行重构。

SipSoup本质是对JsoupXpath重构而来的,里面少部分代码仍然是JsoupXpath的,这里非常感谢JsoupXpath,但是绝大部分代码都被替换了,所以本身SipSoup不算依赖JsoupXpath(组件结构几乎还和JsoupXpath保持了一致)。
目前看来,SipSoup完全兼容JsoupXpath,因为它本身自JsoupXpath发展而来。关于JsoupXpath的相关资料,参考 [JSoupXpath](http://www.oschina.net/p/Jsoupxpath)

以下为改动点

1. 函数重构,JsoupXpath使用反射加载一个类里面的静态函数,SipSoup则是使用接口实现的方式扩展
2. 类扫描,默认函数注册,是通过扫描器自动注册,如果你需要扩展自己的函数,可以将自己的函数实现放到com.virjar.sipsoup.function,就能够自动注册
3. 轴函数,SipSoup不光支持一般的谓语函数扩展,同时支持轴函数,抽取函数扩展。而且允许函数带参数
4. css('cssQuery') css是SipSoup内置的重要轴函数,它可以将css查询表达式传递给css轴,这是SipSoup一个大的突破,他比Xsoup更加容易的实现了css和xpath混合链式抽取
5. 复杂谓语,目前来说,SipSoup对谓语的支持是我发现的最完善的,支持函数嵌套,表达式嵌套,括号嵌套,处理空格,转义等问题。SipSoup的谓语模块,是我花了整整一天写出来的计算器😄
6. 谓语数据类型扩展,在xpath语法中,我定义了数据类型分为: 字符串,数字,运算符,xpath,函数,属性取值动作,布尔类型,复合表达式 这几种类型,其中除运算符以外的其他数据类型都是可以扩展或者卸载的。(boolean类型是开始设计的时候没有的类型,然后通过这个机制注册了boolean,后来又将其添加到了默认类型了)
7. 运算符重载,运算符重载一般c++ 听得挺多,含义就是可以通过重载运算符实现操作符的行为改写。比如"+"加法操作,遇到数字执行加法,遇到字符串执行字符串链接,你可以重写他,遇到数字转化数字,失败使用默认值等等
8. 多xpath组合逻辑,可以实现多个xpath的多重与或计算,对应集合实现交集,并集计算

总之,SipSoup已经是一个高度扩展的Xpath语法分析器,通过灵活的扩展以及Jsoup整合,成为了一个异常强大的xpath工具

demo如下:
1. 格式化输出节点文本 allText [allText()](http://git.oschina.net/virjar/sipsoup/blob/master/src/test/java/com/virjar/sipsoup/AllTextTest.java)
2. position使用,选取所有偶数节点 [position](http://git.oschina.net/virjar/sipsoup/blob/master/src/test/java/com/virjar/sipsoup/PositionFunctionTest.java)
3. 注册新的函数  [RegisterNewFunctionTest.java](http://git.oschina.net/virjar/sipsoup/blob/master/src/test/java/com/virjar/sipsoup/RegisterNewFunctionTest.java)
4. 注册新的操作符 [运算符重载](http://git.oschina.net/virjar/sipsoup/blob/master/src/test/java/com/virjar/sipsoup/RegisterNewOperator.java)
5. 福利,美女爬虫 [实际demo](http://git.oschina.net/virjar/sipsoup/blob/master/src/test/java/com/virjar/sipsoup/XpathSelectTest.java)



风骚语法展示:

```
 <ul class="ad-thumb-list"> 
   <a href="www.java1234.com/test.jpg">这是一个混淆的图片数据</a>
   <li> 
    <div class="inner"> 
     <a href="http://image11.m1905.cn/uploadfile/2015/0608/20150608044330688388.jpg"><img src="http://image14.m1905.cn/uploadfile/2015/0608/thumb_1_147_100_20150608044330688388.jpg" title="" alt="" class="image0" /></a> 
    </div> </li> 
   <li> 
    <div class="inner"> 
     <a href="http://image13.m1905.cn/uploadfile/2015/0608/20150608044328940773.jpg"><img src="http://image11.m1905.cn/uploadfile/2015/0608/thumb_1_147_100_20150608044328940773.jpg" title="" alt="" class="image1" /></a> 
    </div> </li> 
   <li> 
    <div class="inner"> 
     <a href="http://image13.m1905.cn/uploadfile/2015/0608/20150608044328954604.jpg"><img src="http://image13.m1905.cn/uploadfile/2015/0608/thumb_1_147_100_20150608044328954604.jpg" title="" alt="" class="image2" /></a> 
    </div> </li> 
   <ul></ul>
  </ul>
```
这个文本,需要提取所有偶数行的a标签的图片的链接信息,对应xpath表达式可以这么写

``//css('.ad-thumb-list .inner')::a[position(parent(2)) %2 =0]/@href``
语法解释
1. ``css('.ad-thumb-list .inner')::`` 这是css轴的运用,这个表达式定位到了所有的图片数据(其中"\<a href="www.java1234.com/test.jpg"\>这是一个混淆的图片数据</a>"将会被过滤)
2. ``a[position(parent(2)) %2 =0]`` 这是复杂谓语的一个简单应用,首先a\[xxx\]定位到a标签,然后使用parent函数得到他的爷爷节点,(parent函数可以带参数,必须是一个数字,2代表父亲的父亲,也就是得到了li标签)
3. 然后使用position函数得到这个li元素的position偏移,也就是他是第几个li。
4. 最后,让他和2取模,如果结果为0,代表他就是偶数资源

## 具体文档
### 函数
所有支持的函数,可以通过如下demo得到 [解析函数的demo](http://git.oschina.net/virjar/sipsoup/blob/master/src/test/java/com/virjar/sipsoup/FunctionListTest.java)
```
轴函数 :ancestor
轴函数 :ancestorOrSelf
轴函数 :cacheCss
轴函数 :child
轴函数 :css
轴函数 :descendant
轴函数 :descendantOrSelf
轴函数 :followingSibling
轴函数 :followingSiblingOne
轴函数 :parent
轴函数 :precedingSibling
轴函数 :precedingSiblingOne
轴函数 :self
轴函数 :sibling
谓语过滤函数 :abs
谓语过滤函数 :allText
谓语过滤函数 :boolean
谓语过滤函数 :concat
谓语过滤函数 :contains
谓语过滤函数 :false
谓语过滤函数 :first
谓语过滤函数 :hasClass
谓语过滤函数 :last
谓语过滤函数 :lower-case
谓语过滤函数 :matches
谓语过滤函数 :name
谓语过滤函数 :not
谓语过滤函数 :nullToDefault
谓语过滤函数 :parent
谓语过滤函数 :position
谓语过滤函数 :root
谓语过滤函数 :string
谓语过滤函数 :string-length
谓语过滤函数 :substring
谓语过滤函数 :text
谓语过滤函数 :toDouble
谓语过滤函数 :toInt
谓语过滤函数 :true
谓语过滤函数 :try
谓语过滤函数 :upper-case
抽取函数 :allText
抽取函数 :@
抽取函数 :html
抽取函数 :node
抽取函数 :num
抽取函数 :outerHtml
抽取函数 :self
抽取函数 :tag
抽取函数 :text
```
#### 轴函数
轴函数是定义节点塞选域的函数,在一个xpath表达式节点中,轴是可选的。但是如果存在轴,那么他的作用则是根据当前的节点集产生新的一片候选节点集。
SipSoup的轴和标准Xpath的轴保持兼容,但是也有一个地方不一样,就是SipSoup的轴允许带有参数,轴参数目前只支持字符串,可以支持多个字符串的参数。

轴函数列表



|函数名称|参数|作用|
|------:|:------|:------|
|ancestor|无|全部祖先节点 父亲，爷爷 ， 爷爷的父亲...|
|ancestorOrSelf|无|全部祖先节点和自身节点|
|cacheCss|css query表达式|内部实现路由至Jsoup的select,和css轴不一样的是,cacheCss会对css规则进行缓存,在遇到大量同类型网页的解析的时候,可能一个css规则被多次使用,缓存css能够减少css规则编译的消耗,提升性能|
|child|无|直接子节点|
|css|css query表达式|内部实现路由至Jsoup的select|
|descendant|无|全部子代节点 儿子，孙子，孙子的儿子...|
|descendantOrSelf|无|全部子代节点和自身|
|following-sibling|无|节点后面的全部同胞节点following-sibling|
|following-sibling-one|无|返回下一个同胞节点(扩展) 语法 following-sibling-one|
|parent|无|父节点|
|preceding-sibling|无|节点前面的全部同胞节点，preceding-sibling|
|preceding-sibling-one|无|返回前一个同胞节点（扩展），语法 preceding-sibling-one|
|self|无|自身|
|sibling|无|全部同胞（扩展）|

#### 抽取函数
抽取函数用来对结果集进行转换,他是一些简单的数据抽取函数,如提取所有文本,提取某个属性等等,抽取函数目前不对太多。抽取结果可能为元素,也能为字符串。请注意,如果xpath节点中,存在抽取函数,而且抽取函数返回类型为字符串,那么这个函数应该是这个xpath节点链的末尾节点。因为每个抽取节点开始的时候,将会执行过滤,只会将element作为下一轮抽取的输入。

轴函数列表

|函数名称|参数|作用|
|---|---|---|
|allText  |无|递归获取节点内全部的纯文本,将block的html元素转化为换行符,能够保持原有的html的段落结构,但是不能保持布局结构|
|@        |无|抽取当前节点的某个属性,数据内部函数,外界不可直接调用,原有是函数语法识别器不会识别@函数,但是@操作符本身内部是转化抽取函数了|
|html     |无|获取全部节点的内部的html|
|node     |无|获取全部节点的html|
|num      |无|抽取节点自有文本中全部数字|
|outerHtml|无|获取全部节点的 包含节点本身在内的全部html|
|self     |无|放弃抽取,保留自身。xpath默认会对当前节点的子节点进行操作,而有些时候轴函数定位到的节点就是需要的数据了,所以不需要在使用抽取函数计算新节点了,主要用在tag函数不能解决的场景下|
|tag      |tagName|内部函数,xpath 语法中的tag字段,默认路由到此函数处理``//div\[@class\]`` 这里的div在运行时会转化为 ``//tag('div')\[@'class'\]``这两个表达式等价,但是第一个表达式才是xpath常规思路,而且对SipSoup这两个表达式性能完全一样 |
|text     |无|只获取节点自身的子文本|

### 过滤函数
过滤函数用在谓语中,过滤函数非常强大,