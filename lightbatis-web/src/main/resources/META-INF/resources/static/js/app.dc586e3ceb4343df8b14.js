webpackJsonp([1],{"7gYV":function(e,t){},BhiF:function(e,t){},N1zp:function(e,t){},NHnr:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=a("7+uW"),l={render:function(){var e=this.$createElement,t=this._self._c||e;return t("div",{attrs:{id:"app"}},[t("router-view")],1)},staticRenderFns:[]};var i=a("VU/8")({name:"App"},l,!1,function(e){a("p0Q1")},null,null).exports,s=a("/ocq"),r={data:function(){return{formdata:{username:"",password:""}}},methods:{handleLogin:function(){localStorage.getItem("token")?this.$router.push({name:"home"}):"admin"===this.formdata.username.trim()&&"admin"===this.formdata.password.trim()?(this.$message.success("登陆成功"),localStorage.setItem("token","sdsdfsdfsdf"),this.$router.push({name:"home"})):this.$message.error("账号或密码错误")}}},o={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"wrap"},[a("div",{staticClass:"logo"}),e._v(" "),a("el-form",{staticClass:"form",attrs:{"label-position":"top","label-width":"80px",model:e.formdata}},[a("h3",[e._v("用户登录")]),e._v(" "),a("el-form-item",{attrs:{label:"用户名"}},[a("el-input",{model:{value:e.formdata.username,callback:function(t){e.$set(e.formdata,"username",t)},expression:"formdata.username"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"密码"}},[a("el-input",{attrs:{type:"password"},model:{value:e.formdata.password,callback:function(t){e.$set(e.formdata,"password",t)},expression:"formdata.password"}})],1),e._v(" "),a("el-button",{staticClass:"btn",attrs:{type:"primary"},on:{click:function(t){return e.handleLogin()}}},[e._v("登录")])],1)],1)},staticRenderFns:[]};a("VU/8")(r,o,!1,function(e){a("nSZj")},null,null).exports;var c={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-aside",{staticClass:"aside",attrs:{width:"200px"}},[a("el-menu",{staticClass:"menu",attrs:{"default-active":"1"}},[a("el-menu-item",{attrs:{index:"1"}},[a("i",{staticClass:"el-icon-tickets"}),e._v(" "),a("span",{attrs:{slot:"title"},slot:"title"},[e._v("数据层")])]),e._v(" "),a("el-menu-item",{attrs:{index:"2"}},[a("i",{staticClass:"el-icon-menu"}),e._v(" "),a("span",{attrs:{slot:"title"},slot:"title"},[e._v("数据接口层")])]),e._v(" "),a("el-menu-item",{attrs:{index:"3"}},[a("i",{staticClass:"el-icon-document"}),e._v(" "),a("span",{attrs:{slot:"title"},slot:"title"},[e._v("服务层")])]),e._v(" "),a("el-menu-item",{attrs:{index:"4"}},[a("i",{staticClass:"el-icon-edit-outline"}),e._v(" "),a("span",{attrs:{slot:"title"},slot:"title"},[e._v("业务层")])]),e._v(" "),a("el-menu-item",{attrs:{index:"5"}},[a("i",{staticClass:"el-icon-setting"}),e._v(" "),a("span",{attrs:{slot:"title"},slot:"title"},[e._v("控制层")])])],1)],1)},staticRenderFns:[]};var u=a("VU/8")({name:"side-bar"},c,!1,function(e){a("N1zp")},null,null).exports,d={name:"alert",props:{show:{type:Boolean}},data:function(){return{}},methods:{cancle:function(){this.$emit("showfn1",!1)},confirm:function(){this.$emit("showfn1",!1)},handleClose:function(){this.$emit("showfn1",!1)}}},p={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-dialog",{attrs:{title:"提示",visible:e.show,width:"30%","before-close":e.handleClose,"close-on-click-modal":"false"},on:{"update:visible":function(t){e.show=t}}},[a("span",[e._v("这是一段信息")]),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:e.cancle}},[e._v("取 消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:e.confirm}},[e._v("确 定")])],1)])],1)},staticRenderFns:[]};var m={name:"header",data:function(){return{show:!1,input1:"",input2:"",checkList:[]}},methods:{onClick:function(){this.show=!0},showfn:function(e){this.show=e}},components:{Alert:a("VU/8")(d,p,!1,function(e){a("7gYV")},null,null).exports}},v={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"header"},[e.show?a("Alert",{attrs:{show:e.show},on:{showfn1:e.showfn}},[e._v("xiashi")]):e._e(),e._v(" "),a("el-row",[a("el-col",{attrs:{span:8}},[a("div",{staticClass:"grid-content bg-purple-light"},[a("span",[e._v("输出文件夹:")]),e._v(" "),a("el-input",{staticStyle:{width:"300px"},attrs:{placeholder:"请输入内容"},model:{value:e.input1,callback:function(t){e.input1=t},expression:"input1"}})],1)]),e._v(" "),a("el-col",{attrs:{span:8}},[a("div",{staticClass:"grid-content bg-purple-light"},[a("span",[e._v("基础包名:")]),e._v(" "),a("el-input",{staticStyle:{width:"300px"},attrs:{placeholder:"请输入内容"},model:{value:e.input2,callback:function(t){e.input2=t},expression:"input2"}})],1)]),e._v(" "),a("el-col",{attrs:{span:8}},[a("div",{staticClass:"grid-content bg-purple-light"},[a("el-checkbox",{attrs:{label:"履盖现有的输出类"}})],1)])],1)],1)},staticRenderFns:[]};var b=a("VU/8")(m,v,!1,function(e){a("BhiF")},null,null).exports,h={name:"topMenu",data:function(){return{activeIndex:"1"}},methods:{handleSelect:function(e,t){console.log(e,t)}}},f={render:function(){var e=this.$createElement,t=this._self._c||e;return t("el-menu",{staticClass:"el-menu-demo",attrs:{"default-active":this.activeIndex,mode:"horizontal","background-color":"#545c64","text-color":"#fff","active-text-color":"#ffd04b"},on:{select:this.handleSelect}},[t("el-menu-item",{attrs:{index:"1"}},[this._v("数据层")]),this._v(" "),t("el-menu-item",{attrs:{index:"3",disabled:""}},[this._v("设置")])],1)},staticRenderFns:[]};var g=a("VU/8")(h,f,!1,function(e){a("rRJ1")},"data-v-e3db602a",null).exports,_=a("Xxa5"),y=a.n(_),w=a("exGp"),S=a.n(w),T=a("//Fk"),C=a.n(T),x=a("mtWM"),k=a.n(x),N=a("zL8q"),E=a.n(N),$=window.location.protocol+"//"+window.location.hostname+":"+window.location.port,z=(window.location,k.a.create({baseURL:$,timeout:5e3}));z.interceptors.request.use(function(e){return e},function(e){return console.log(e),C.a.reject(e)}),z.interceptors.response.use(function(e){var t=e;return 200!==t.status?(Object(N.Message)({message:t.message||"Error",type:"error",duration:5e3}),console.log(t),C.a.reject(new Error(t.message||"Error"))):t.data},function(e){return console.log("err"+e),Object(N.Message)({message:e.message,type:"error",duration:5e3}),C.a.reject(e)});var A=z;function M(){return A({url:"/dal/list/entities",method:"get"})}function j(e,t,a){return A({url:"/dal/generated?type="+a,method:"post",data:{setting:e,tableSchema:t}})}var P={name:"batchPanel",props:{generateSetting:{type:Object}},data:function(){return{selectedTable:{},tables:[]}},methods:{tableSelected:function(e){console.log(e),this.selectedTable=e,-1===this.tables.findIndex(function(t,a,n){return t.tableName===e.tableName})&&this.tables.push(e)},removeSelected:function(){var e=this.$refs.tableList.selection,t=this.tables;e.forEach(function(e){var a=t.findIndex(function(t,a,n){return t.tableName===e.tableName});a>-1&&t.splice(a,1)})},batchGeneratedSubmit:function(){var e=this;(function(e,t){return A({url:"/dal/generated/batch",method:"post",data:{setting:e,tableSchemas:t}})})(this.generateSetting,this.tables).then(function(t){e.$message({message:"操作成功",type:"success"})})}},mounted:function(){}},R={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("div",{staticStyle:{"margin-bottom":"5px"}},[a("el-button-group",[a("el-button",{attrs:{icon:"el-icon-remove"},on:{click:function(t){return e.removeSelected()}}},[e._v("取消所选的表")]),e._v(" "),a("el-button",{attrs:{icon:"el-icon-finished"},on:{click:function(t){return e.batchGeneratedSubmit()}}},[e._v("批量生成")])],1)],1),e._v(" "),a("el-table",{ref:"tableList",staticStyle:{width:"100%"},attrs:{data:e.tables,border:"","highlight-current-row":"","max-height":"500"}},[a("el-table-column",{attrs:{type:"selection",prop:"tableName",width:"55"}}),e._v(" "),a("el-table-column",{attrs:{fixed:"",prop:"tableName",label:"表名",width:"180"}}),e._v(" "),a("el-table-column",{attrs:{prop:"common",label:"名称"}})],1)],1)},staticRenderFns:[]};var U=a("VU/8")(P,R,!1,function(e){a("fU3n")},"data-v-c21f74c8",null).exports,V=a("fZjL"),F=a.n(V),D={name:"entityPanel",props:{selectedTable:{type:Object},selectedTableEntity:{type:Object},generateSetting:{type:Object}},data:function(){return{tableAnnotations:{}}},methods:{generateSubmit:function(e){var t=this,a=this;F()(this.tableAnnotations).forEach(function(e){var n=t.tableAnnotations[e];a.selectedTable[n]=e});var n=this;j(this.generateSetting,a.selectedTable,"entity").then(function(e){t.$message({message:"操作成功",type:"success"}),n.$emit("generated")})}},mounted:function(){this.tableAnnotations[this.selectedTable.logicDelteColumn]="logicDelteColumn",this.tableAnnotations[this.selectedTable.revisionColumn]="revisionColumn",this.tableAnnotations[this.selectedTable.createdTimeColumn]="createdTimeColumn",this.tableAnnotations[this.selectedTable.updatedTimeColumn]="updatedTimeColumn"}},L={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[null==e.selectedTable?a("div",[a("el-alert",{attrs:{title:"还没有选择要操作的表",type:"success",effect:"dark"}})],1):e._e(),e._v(" "),null!==e.selectedTable?a("div",[a("el-form",{attrs:{"label-position":"right",size:"mini",model:e.selectedTable}},[a("div",{staticClass:"demo-input-suffix"},[a("span",{staticClass:"label"},[e._v("输出的类名")]),e._v(" "),a("el-input",{attrs:{placeholder:"输出包名",size:"mini"},model:{value:e.selectedTable.entityPackageName,callback:function(t){e.$set(e.selectedTable,"entityPackageName",t)},expression:"selectedTable.entityPackageName"}}),e._v(" "),a("el-input",{attrs:{placeholder:"实体类名",size:"mini"},model:{value:e.selectedTable.entityName,callback:function(t){e.$set(e.selectedTable,"entityName",t)},expression:"selectedTable.entityName"}})],1),e._v(" "),a("el-input",{attrs:{size:"mini",placeholder:"已经生成的实体类",value:e.selectedTableEntity.beanClzName,disabled:!0}},[a("template",{slot:"prepend"},[e._v("已经生成的实体类")])],2),e._v(" "),a("el-form-item",{staticStyle:{"margin-top":"5px"}},[a("el-button",{attrs:{type:"primary"},on:{click:e.generateSubmit}},[e._v("立即创建实体类")])],1)],1),e._v(" "),a("el-table",{staticStyle:{width:"100%"},attrs:{border:"",height:"600",data:e.selectedTable.columns}},[a("el-table-column",{attrs:{label:"字段",property:"columnName",width:"150"}}),e._v(" "),a("el-table-column",{attrs:{property:"common",label:"名称",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{property:"typeName",label:"类型",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{property:"length",label:"长度",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{property:"propertyName",label:"实体类属性名称"}}),e._v(" "),a("el-table-column",{attrs:{label:"添加属性注释"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-select",{attrs:{size:"mini",placeholder:"请选择注释"},model:{value:e.tableAnnotations[t.row.columnName],callback:function(a){e.$set(e.tableAnnotations,t.row.columnName,a)},expression:"tableAnnotations[scope.row.columnName]"}},[a("el-option",{attrs:{label:"逻辑删除字段",value:"logicDelteColumn"}}),e._v(" "),a("el-option",{attrs:{label:"乐观锁字段",value:"revisionColumn"}}),e._v(" "),a("el-option",{attrs:{label:"更新日期时间字段",value:"updatedTimeColumn"}}),e._v(" "),a("el-option",{attrs:{label:"创建日期时间字段",value:"createdTimeColumn"}})],1)]}}],null,!1,3431578365)}),e._v(" "),a("el-table-column",{attrs:{property:"columnClz",label:"实体类属性类型"}})],1)],1):e._e()])},staticRenderFns:[]};var O=a("VU/8")(D,L,!1,function(e){a("XtmU")},"data-v-88d92dac",null).exports,B={data:function(){return{activeTab:"entity",tables:[],selectedTable:null,selectedTableEntity:null,selectedEntityMapper:{mapperScript:"",mapperClass:"mapper",tableName:""},selectedEntityService:{serviceScript:"",serviceClass:"service",tableName:""},mapperShowAble:!1,serviceShowable:!1,selectedMapperScript:"",tableEntities:[],generateSetting:{outDir:"",outPackage:"",overwrite:!0}}},methods:{handleCurrentChange:function(e){this.selectedTable=e,"mapper"===this.activeTab&&this.updateMapper(),"service"===this.activeTab&&this.updateService();var t=e.tableName,a=this;a.selectedTableEntity={},this.tableEntities.forEach(function(e){e.table==t&&(a.selectedTableEntity=e)}),"batch"===this.activeTab&&this.$refs.batchPanel.tableSelected(e)},generateSubmit:function(e){var t=this;j(this.generateSetting,this.selectedTable,this.activeTab).then(function(e){t.$message({message:"操作成功",type:"success"}),M().then(function(e){t.tableEntities=e})})},tabClick:function(e,t){"mapper"==this.activeTab&&this.updateMapper(),"service"==this.activeTab&&this.updateService()},updateMapper:function(){var e=S()(y.a.mark(function e(){var t,a;return y.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return null!=this.selectedTableEntity&&(this.selectedTableEntity.table,this.selectedEntityMapper.tableName),this.mapperShowAble=!1,t=this,e.next=5,n=this.selectedTable,void 0,l=n,console.log(l),A({url:"/dal/get/mapper",method:"post",data:l});case 5:a=e.sent,t.selectedEntityMapper=a,this.mapperShowAble=!0;case 8:case"end":return e.stop()}var n,l},e,this)}));return function(){return e.apply(this,arguments)}}(),updateService:function(){var e=S()(y.a.mark(function e(){var t,a;return y.a.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return null!=this.selectedTableEntity&&(this.selectedTableEntity.table,this.selectedEntityService.tableName),this.serviceShowable=!1,t=this,e.next=5,n=this.selectedTable,void 0,l=n,console.log(l),A({url:"/dal/get/service",method:"post",data:l});case 5:a=e.sent,t.selectedEntityService=a,this.serviceShowable=!0;case 8:case"end":return e.stop()}var n,l},e,this)}));return function(){return e.apply(this,arguments)}}(),generatedHandle:function(){var e=this;M().then(function(t){e.tableEntities=t})}},mounted:function(){var e=this;M().then(function(t){e.tableEntities=t}),A({url:"/dal/table/entities",method:"get"}).then(function(t){e.tables=t,console.log(t)}),A({url:"/dal/generate/setting",method:"get"}).then(function(t){e.generateSetting=t,console.log(t)})},components:{BatchPanel:U,EntityPanel:O}},I={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"main"},[a("el-row",{staticStyle:{margin:"5px"}},[a("el-col",{attrs:{span:6}},[a("el-card",{staticClass:"box-card"},[a("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[a("span",[e._v("当前库所有表")])]),e._v(" "),a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tables,border:"","highlight-current-row":"",height:"600"},on:{"current-change":e.handleCurrentChange}},[a("el-table-column",{attrs:{fixed:"",prop:"tableName",label:"表名",width:"180"}}),e._v(" "),a("el-table-column",{attrs:{prop:"common",label:"名称"}})],1)],1)],1),e._v(" "),a("el-col",{attrs:{span:18}},[a("el-row",[a("el-col",{attrs:{span:24}},[a("el-form",{staticClass:"setting-form",staticStyle:{"padding-top":"15px"},attrs:{inline:!0,model:e.generateSetting,"label-position":"left",size:"mini","label-width":"100px"}},[a("el-form-item",{staticStyle:{alignment:"left"},attrs:{label:"输出文件夹"}},[a("el-input",{staticStyle:{width:"400px"},attrs:{placeholder:"输出文件夹"},model:{value:e.generateSetting.outDir,callback:function(t){e.$set(e.generateSetting,"outDir",t)},expression:"generateSetting.outDir"}})],1),e._v(" "),a("el-form-item",[a("el-checkbox",{attrs:{label:"履盖现有的输出类"},model:{value:e.generateSetting.overwrite,callback:function(t){e.$set(e.generateSetting,"overwrite",t)},expression:"generateSetting.overwrite"}})],1)],1)],1)],1),e._v(" "),a("el-tabs",{staticStyle:{"margin-left":"5px"},attrs:{type:"border-card"},on:{"tab-click":e.tabClick},model:{value:e.activeTab,callback:function(t){e.activeTab=t},expression:"activeTab"}},[a("el-tab-pane",{attrs:{label:"实体类管理 (DO)",name:"entity"}},[null!==e.selectedTable&&"entity"==e.activeTab?a("EntityPanel",{attrs:{selectedTable:e.selectedTable,selectedTableEntity:e.selectedTableEntity,generateSetting:e.generateSetting},on:{generated:e.generatedHandle}}):e._e()],1),e._v(" "),a("el-tab-pane",{attrs:{label:"Mapper 管理(Mapper)",name:"mapper"}},[null!==e.selectedTable&&"mapper"==e.activeTab?a("div",[a("el-form",{attrs:{"label-position":"right",size:"mini",model:e.selectedTable}},[a("div",{staticClass:"demo-input-suffix"},[a("span",{staticClass:"label"},[e._v("输出的类名")]),e._v(" "),a("el-input",{attrs:{placeholder:"输出包名",size:"mini"},model:{value:e.selectedTable.mapperPackageName,callback:function(t){e.$set(e.selectedTable,"mapperPackageName",t)},expression:"selectedTable.mapperPackageName"}}),e._v(" "),a("el-input",{attrs:{placeholder:"实体类名",size:"mini"},model:{value:e.selectedTable.mapperClzName,callback:function(t){e.$set(e.selectedTable,"mapperClzName",t)},expression:"selectedTable.mapperClzName"}})],1),e._v(" "),a("el-input",{attrs:{size:"mini",placeholder:"已经生成的 Mapper 类",value:e.selectedTableEntity.beanClzName,disabled:!0}},[a("template",{slot:"prepend"},[e._v("已经生成的实体类")])],2),e._v(" "),a("el-form-item",{staticStyle:{"margin-top":"5px"}},[a("el-button",{attrs:{type:"primary"},on:{click:e.generateSubmit}},[e._v("立即创建 Mapper")])],1)],1),e._v(" "),a("el-card",[e.mapperShowAble?a("pre",{directives:[{name:"highlightjs",rawName:"v-highlightjs"}],key:e.selectedEntityMapper.mapperClass},[a("code",{staticClass:"java"},[e._v(e._s(e.selectedEntityMapper.mapperScript))])]):e._e()])],1):e._e()]),e._v(" "),a("el-tab-pane",{attrs:{label:"Service 管理 (Service)",name:"service"}},["service"===e.activeTab?a("div",[a("el-form",{attrs:{"label-position":"right",size:"mini",model:e.selectedTable}},[a("div",{staticClass:"demo-input-suffix"},[a("span",{staticClass:"label"},[e._v("输出的类名")]),e._v(" "),a("el-input",{attrs:{placeholder:"输出包名",size:"mini"},model:{value:e.selectedTable.servicePackageName,callback:function(t){e.$set(e.selectedTable,"servicePackageName",t)},expression:"selectedTable.servicePackageName"}}),e._v(" "),a("el-input",{attrs:{placeholder:"服务类名",size:"mini"},model:{value:e.selectedTable.serviceClzName,callback:function(t){e.$set(e.selectedTable,"serviceClzName",t)},expression:"selectedTable.serviceClzName"}})],1),e._v(" "),a("el-input",{attrs:{size:"mini",placeholder:"已经生成的 Service 类",value:e.selectedTableEntity.serviceClzName,disabled:!0}},[a("template",{slot:"prepend"},[e._v("已经生成的服务类")])],2),e._v(" "),a("el-form-item",{staticStyle:{"margin-top":"5px"}},[a("el-button",{attrs:{type:"primary"},on:{click:e.generateSubmit}},[e._v("立即创建 Service")])],1)],1),e._v(" "),a("el-card",[e.serviceShowable?a("pre",{directives:[{name:"highlightjs",rawName:"v-highlightjs"}],key:e.selectedEntityService.serviceClass},[a("code",{staticClass:"java"},[e._v(e._s(e.selectedEntityService.serviceScript))])]):e._e()])],1):e._e()]),e._v(" "),a("el-tab-pane",{attrs:{label:"Controller 管理",name:"controller"}},[e._v("Controller 管理")]),e._v(" "),a("el-tab-pane",{attrs:{label:"批量处理",name:"batch"}},[a("BatchPanel",{ref:"batchPanel",attrs:{generateSetting:e.generateSetting}})],1)],1)],1)],1)],1)},staticRenderFns:[]};var q={components:{SideBar:u,Header:b,Main:a("VU/8")(B,I,!1,function(e){a("Vqfk")},"data-v-ec78359e",null).exports,TopMenu:g}},H={render:function(){var e=this.$createElement,t=this._self._c||e;return t("el-container",{staticClass:"wrap-content"},[t("el-header",[t("TopMenu")],1),this._v(" "),t("el-main",[t("Main")],1)],1)},staticRenderFns:[]};var G=a("VU/8")(q,H,!1,function(e){a("cAo7")},"data-v-37c91084",null).exports;n.default.use(s.a);var J=new s.a({routes:[{name:"home",path:"/",component:G},{name:"home",path:"/home",component:G}]}),Q=(a("tvR6"),a("NYWk"),a("5CJf")),W=a.n(Q),Y=a("Zpgj"),Z=a.n(Y),X=a("Gu7T"),K=a.n(X),ee=function(e){var t=new e({methods:{emit:function(e){for(var t=arguments.length,a=Array(t>1?t-1:0),n=1;n<t;n++)a[n-1]=arguments[n];this.$emit.apply(this,[e].concat(K()(a)))},on:function(e,t){this.$on(e,t)},off:function(e,t){this.$off(e,t)}}});e.prototype.$bus=t};a("P+Qf");n.default.use(ee),n.default.use(E.a),n.default.use(W.a,{languages:{java:Z.a}}),new n.default({el:"#app",router:J,components:{App:i},template:"<App/>"})},NYWk:function(e,t){},"P+Qf":function(e,t){},Vqfk:function(e,t){},XtmU:function(e,t){},cAo7:function(e,t){},fU3n:function(e,t){},nSZj:function(e,t){},p0Q1:function(e,t){},rRJ1:function(e,t){},tvR6:function(e,t){}},["NHnr"]);
//# sourceMappingURL=app.dc586e3ceb4343df8b14.js.map