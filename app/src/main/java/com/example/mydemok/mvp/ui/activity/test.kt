package com.example.mydemok.mvp.ui.activity

class test {
    fun main(args: Array<String>) {
        inlineFunction({ println("calling inline functions")
            return // compile time error
        },{ println("next parameter in inline functions")})
    }//原文出自【易百教程】，商业转载请联系作者获得授权，非商业请保留原文链接：https://www.yiibai.com/kotlin/kotlin-inline-function.html#article-start




    inline fun inlineFunction(myFun: () -> Unit, nxtFun: () -> Unit) {
        myFun()
        nxtFun()
        print("内联函数内的代码")
    }


}