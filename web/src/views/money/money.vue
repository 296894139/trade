<template>
    <div>
        <p class="title">交纳保证金</p>
        <div class="content-all">
            <div class="cont-one">
                <p class="line"></p>
                <p>代理等级选择</p>
            </div>
            <div class="cont-two">
                <div :class="{active:shows==1}" @click="choose(1)">
                    <p class="two-title">省级代理</p>
                    <p class="two-cont">
                        ￥ <span class="two-pay" ref="one">0.1</span>
                    </p>
                </div>
                <div :class="{active:shows==2}" @click="choose(2)">
                    <p class="two-title">市级代理</p>
                    <p class="two-cont">
                        ￥ <span class="two-pay" ref="two">0.1</span>
                    </p>
                </div>
                <div :class="{active:shows==3}" @click="choose(3)">
                    <p class="two-title">县级代理</p>
                    <p class="two-cont">
                        ￥ <span class="two-pay" ref="three">0.1</span>
                    </p>
                </div>
            </div>
            <div class="btn" @click="sure">
                确认交纳
            </div>
        </div>

        <!-- 弹框信息 -->
        <div class="show-message" v-show="isshow">
            <div class="show-box">
                <div class="show-one">
                    <p class="show-line"></p>
                    <p>确认充值</p>
                    <p class="show-line"></p>
                </div>
                <div class="show-two">
                    <p>市级代理</p>
                    <br>
                    <p>所需金额：{{needMoney}}元</p>
                </div>
                <div class="show-three">
                    <div class="show-btn" @click="cancel">取消</div>
                    <div class="show-btn1" @click="payfor">确认交纳</div>
                </div>
            </div>
        </div>

        <div v-html ='apply'>
             {{apply}}
        </div>
    </div>
</template>

<script>
import {
  getAll,
  postData,
  getDataByID,
  putData,

} from '@/api/dbhelper'
import { mapGetters } from "vuex";
export default {
    name:"money",
    data(){
        return{
            isshow:false,
            apply:'',
            shows:1,
            needMoney:'0.01'
        }
    },
          computed: {
				  ...mapGetters(["shopId"]),
    headers() {
      return {
        'X-Litemall-Admin-Token': getToken()
      }
    }
  },
  created(){
      console.log(this.shopId,'xxx');
  },
    methods:{
        choose(num){
            if(num==1){
                this.shows=1
                console.log(this.$refs.one.innerText,'1111')
                this.$refs.one.innerText=1
            }else if(num==2){
                console.log(this.$refs.two,'2222')
                this.shows=2
                this.$refs.two.innerText=2
            }else{
                console.log(this.$refs.three,'3333')
                this.shows=3
                this.$refs.three.innerText=3
            }
        },
        sure(){
            this.isshow=true;
        },
        cancel(){
            this.isshow=false;
        },
        payfor() {
            var data={
                totalAmount:this.needMoney,
                subject :'市级代理',
                shopId:this.shopId
            }
      
            postData("shop/apply",data).then(response=>{
            if(response.data.errno===0){
                this.apply = response.data.data
                this.$nextTick(()=> {
                            document.forms[0].submit()
                    })
            }
                
            }).catch(error =>{
                console.log(error)
            })
            this.isshow=false;
        },
    }
}
</script>

<style scoped>
/* 弹框内容 */
.show-message{
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    margin: auto;
    background: rgba(0, 0, 0, 0.4);
}
.show-box{
    width: 500px;
    height: 300px;
    background: white;
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    margin: auto;
    border-radius: 10px;
}
.show-one{
    width: 300px;
    height: 100px;
    line-height: 100px;
    display: flex;
    justify-content: space-between;
    margin: 0 auto;
    color: gray;
    font-size: 20px;
}
.show-line{
    width: 100px;
    height: 1px;
    background: gray;
    margin-top: 50px;
}
.show-two{
    width: 300px;
    padding: 20px;
    text-align: center;
    font-weight: 700;
    margin: 0 auto;
}
.show-three{
    width: 300px;
    margin: 0 auto;
    margin-top: 20px;
    display: flex;
    justify-content: space-between;
}
.show-btn{
    width: 100px;
    height: 30px;
    text-align: center;
    line-height: 30px;
    border-radius: 15px;
    border: 1px solid #ff667f;
    cursor: pointer;
}
.show-btn1{
    width: 100px;
    height: 30px;
    text-align: center;
    line-height: 30px;
    color: white;
    border-radius: 15px;
    border: 1px solid #ff667f;
    background: #ff3657;
    cursor: pointer;
}
/* 基本样式 */
*{
    margin: 0;
    padding: 0;
}
    .title{
        width: 100%;
        height: 100px;
        text-align: center;
        line-height: 100px;
        font-size: 28px;
    }
    .content-all{
        width: 90%;
        margin: 0 auto;
    }
    .cont-one{
        width: 100%;
        height: 20px;
        line-height: 20px;
        display: flex;
    }
    .line{
        width: 5px;
        height: 20px;
        background: #ff3657;
        margin-right: 10px;
    }
    .cont-two{
        width: 800px;
        height: 200px;
        margin: 0 auto;
        margin-top: 50px;
        display: flex;
        justify-content: space-between;
    }
    .cont-two div{
        width: 200px;
        height: 150px;
        border: 2px solid #ff3657;
        border-radius: 5px;
        cursor: pointer;
    }
    .two-title{
        width: 100%;
        height: 60px;
        text-align: center;
        line-height: 60px;
        font-weight: 700;
        font-size: 18px;
        margin-top: 10px;
    }
    .two-cont{
        width: 100%;
        text-align: center;
        font-weight: 700;
    }
    .two-pay{
        font-size: 40px;
        color: #ff3657;
        font-weight: normal;
    }
    .btn{
        width: 150px;
        height: 30px;
        text-align: center;
        line-height: 30px;
        color: white;
        background: #ff3657;
        margin: 0 auto;
        border-radius: 5px;
        cursor: pointer;
    }
    .active{
        background: #fec1cb;
    }
</style>


