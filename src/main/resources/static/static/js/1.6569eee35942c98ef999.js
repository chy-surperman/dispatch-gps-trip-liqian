webpackJsonp([1],{"5Jx2":function(e,t){},Uf9z:function(e,t,i){"use strict";var r=i("02xr"),a=i("uxtY"),n=i("/IwO"),o=new n.AMapManager,s={name:"locusMap",data:function(){return{gpsSign:"",zoom:12,center:[112.998758,28.182349],amapManager:o,events:{init:this.initPathSimplifierIns},map:{},pathSimplifierIns:{},pathNavigator:{},pathData:[{name:"轨迹0",path:[]}],startMark:{},endMark:{},gpsData:[]}},created:function(){},mounted:function(){},methods:{initPathSimplifierIns:function(e){this.map=e;var t=this;AMapUI.loadUI(["misc/PathSimplifier"],function(i){t.createPathSimplifier(i,e)}),AMapUI.loadUI(["overlay/SimpleMarker"],function(i){t.startMark=new i({iconLabel:"始",iconStyle:"red",map:e}),t.endMark=new i({iconLabel:"终",iconStyle:"blue",map:e})})},createPathNavigator:function(){this.pathNavigator=this.pathSimplifierIns.createPathNavigator(0,{loop:!1,speed:120}),console.log(this.pathNavigator),this.pathNavigator.on("move",function(e){console.log(e.target.cursor.idx)})},createPathSimplifier:function(e,t){console.log("init map createPathSimplifier"),this.pathSimplifierIns=new e({zIndex:100,map:t,getPath:function(e,t){return e.path},getHoverTitle:function(e,t,i){return i>=0?e.name+"，点:"+i+"/"+e.path.length:e.name+"，GPS点数量"+e.path.length},renderOptions:{pathLineStyle:{strokeStyle:"#409EFF",lineWidth:6,dirArrowStyle:!0}}})},setData:function(){this.pathSimplifierIns.setData(this.pathData),this.pathSimplifierIns.render()},destoryData:function(){null!=this.vehicleInfoWindow&&(this.vehicleInfoWindow.close(),this.map.remove(this.vehicleInfoWindow)),this.gpsData=null},addWorkplan:function(){this.$emit("addWorkplan",{sign:this.gpsSign})},query:function(e){var t=this,i=this.$loading({lock:!0,text:"Loading",spinner:"el-icon-loading",background:"rgba(0, 0, 0, 0.7)"});this.destoryData(),e.filterDrift=0,Object(r.w)(e).then(function(e){if(i.close(),e.code==a.b){if(0==e.result.length)return void t.$message.error("该时间段内车辆无轨迹数据");t.pathData[0].path=[],e.result.forEach(function(e){var i=[e.longitude,e.latitude];e.lngLat=i,t.pathData[0].path.push(i)}),t.gpsData=e.result,t.setData(),t.startMark.setPosition(t.pathData[0].path[0]),t.endMark.setPosition(t.pathData[0].path[e.result.length-1])}else t.$message.error(e.define)})},createWorkplanGps:function(e){var t=this,i=this.$loading({lock:!0,text:"Loading",spinner:"el-icon-loading",background:"rgba(0, 0, 0, 0.7)"});this.destoryData(),Object(r.h)(e).then(function(e){i.close(),e.code==a.b?(0==e.result.length&&t.$message.error("无轨迹数据生成"),t.pathData[0].path=[],e.result.datas.forEach(function(e){var i=[e.longitude,e.latitude];e.lngLat=i,t.pathData[0].path.push(i)}),t.gpsData=e.result.datas,t.setData(),t.startMark.setPosition(t.pathData[0].path[0]),t.endMark.setPosition(t.pathData[0].path[e.result.length-1])):t.$message.error(e.define)})}}},l={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("div",{staticClass:"amap-page-container"},[i("el-amap",{staticClass:"amap-demo",attrs:{vid:"amapDemo",center:e.center,"map-manager":e.amapManager,zoom:e.zoom,events:e.events}})],1),e._v(" "),i("div",{staticStyle:{margin:"15px 0"}},[i("el-button",{attrs:{size:"mini",type:"primary"},on:{click:e.addWorkplan}},[e._v("添加趟次")])],1),e._v(" "),i("div",{staticStyle:{width:"100%",height:"1px"}})])},staticRenderFns:[]};var u=i("VU/8")(s,l,!1,function(e){i("iSWh")},"data-v-378ba2a8",null);t.a=u.exports},WKmw:function(e,t,i){"use strict";var r={name:"timeInputComponent",props:["width","time"],data:function(){return{hour:"",minute:""}},model:{prop:"value",event:"time"},mounted:function(){if(this.time&&-1!=this.time.indexOf(":")){var e=this.time.split(":");this.hour=e[0],this.minute=e[1]}},computed:{value:function(){return this.hour&&this.minute?(parseInt(this.hour)<10?"0"+parseInt(this.hour):this.hour)+":"+(parseInt(this.minute)<10?"0"+parseInt(this.minute):this.minute):""}},methods:{hourChange:function(e){var t=e.target.value;if(!this.isValidInput(t))return this.hour="",this.$emit("time",this.value),!1;if(1==t.length&&parseInt(t)>2&&(this.hour="0"+t,this.$refs.minute.focus(),this.$refs.minute.select()),t.length>=2){var i=t.substr(0,2);i>=24?(i=t.substr(0,1),this.hour=i):(this.hour=i,this.$refs.minute.focus(),this.$refs.minute.select())}this.$emit("time",this.value)},minuteChange:function(e){var t=e.target.value;if(!this.isValidInput(t))return this.minute="",this.$emit("time",this.value),!1;if(t.length>=2){var i=t.substr(0,2);parseInt(i)>59&&(i=t.substr(1,1)),this.minute=i}this.$emit("time",this.value)},isValidInput:function(e){return null!=e&&void 0!=e&&0!=e.length&&!!/^[0-9]+$/.test(e)}}},a={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"time-input-box"},[i("input",{directives:[{name:"model",rawName:"v-model",value:e.hour,expression:"hour"}],style:{width:e.width+"px"},attrs:{placeholder:"时"},domProps:{value:e.hour},on:{input:[function(t){t.target.composing||(e.hour=t.target.value)},e.hourChange]}}),e._v(" "),i("span",[e._v(":")]),e._v(" "),i("input",{directives:[{name:"model",rawName:"v-model",value:e.minute,expression:"minute"}],ref:"minute",style:{width:e.width+"px"},attrs:{placeholder:"分"},domProps:{value:e.minute},on:{input:[function(t){t.target.composing||(e.minute=t.target.value)},e.minuteChange]}})])},staticRenderFns:[]};var n=i("VU/8")(r,a,!1,function(e){i("5Jx2")},"data-v-80a66156",null);t.a=n.exports},iSWh:function(e,t){},y3EO:function(e,t){},yjVO:function(e,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=i("Dd8w"),a=i.n(r),n=i("02xr"),o=i("uxtY"),s=i("Uf9z"),l=i("WKmw"),u=i("+v9d"),c={name:"departureWorkplan",components:{workplanLocus:s.a,timeInputComponent:l.a},data:function(){return{date:"",routeName:"",isQueryAll:!0,queryDate:"",addModel:"arrived",lineRunTime:{up:0,down:0,arrival:0},wpLocusQuery:{startTime:"",endTime:"",vehicleId:""},queryWorkplan:{date:"",starttag:"0",routeName:"",vehicleId:"",driverId:""},departureData:[],routeList:[],vehicles:[],drivers:[],height:500,cancelWorkplan:null}},mounted:function(){var e=this;document.onkeydown=function(t){13==t.keyCode&&e.saveWorkplan()}},created:function(){this._getRouteMsg(),this.isQueryAll&&(this._getVehicle(),this._getDriver()),this.queryWorkplan.date=Object(u.a)(new Date,"yyyy-MM-dd")},methods:{selectRow:function(e){this.selection=e},routeChange:function(e){this.routeName=e,this._getRouteRunTime(),this.isQueryAll&&(this.vehicles.length<200&&this._getVehicle(),this.drivers.length<200&&this._getDriver())},_getRouteMsg:function(){var e=this;Object(n.r)().then(function(t){console.log(t),t.code==o.b&&(e.routeList=t.result)})},_getRouteRunTime:function(){var e=this,t={routeName:this.routeName};Object(n.s)(t).then(function(t){t.code==o.b&&(e.lineRunTime.up=t.result.up,e.lineRunTime.down=t.result.down)})},_getRunningVehicle:function(){var e=this,t=a()({},this.queryWorkplan);t.routeName=this.routeName,Object(n.i)(t).then(function(t){t.code==o.b&&(e.departureData=e.handlerFixedWorkplans(t.result))})},_getVehicle:function(){var e=this,t={};this.isQueryAll||(t.routeName=this.routeName),Object(n.n)(t).then(function(t){console.log(t),t.code==o.b&&(e.vehicles=t.result)})},_getDriver:function(){var e=this,t={};this.isQueryAll||(t.routeName=this.routeName),Object(n.l)(t).then(function(t){t.code==o.b&&(e.drivers=t.result)})},handlerFixedWorkplans:function(e){if(null==e||0==e.length)return this.$message.info("无未到达排班"),[];for(var t=0;t<e.length;t++)e[t]=this.handlerWorkplanEle(e[t]);return e},handlerWorkplanEle:function(e){var t=Object(u.a)(new Date(e.departureTime),"hh:mm"),i=t.split(":");if(e.depHour=i[0],e.depMinute=i[1],e.departureMinute=t,null==e.driverId||e.vehicleId,e.isEdit=!1,e.model="edit",e.createNewGps="1",e.direction="0"==e.starttag?"上":"下",null==e.arrivalString||""==e.arrivalString)e.arrHour="",e.arrMinute="",e.arrivalMinute="";else{e.arrivalMinute=Object(u.a)(new Date(e.arrivalTime),"hh:mm");var r=e.arrivalString.split(":");e.arrHour=r[0],e.arrMinute=r[1]}return e.reasonType="",e.reasonDetail="",e},queryNotArrive:function(){null!=this.queryWorkplan.date&&""!=this.queryWorkplan.date?null!=this.queryWorkplan.starttag&&""!=this.queryWorkplan.starttag?this._getRunningVehicle():this.$message.error("请先选择方向"):this.$message.error("请先选择日期")},queryLocus:function(e){var t={startDate:this.queryWorkplan.date+" "+e.departureMinute+":00",endDate:this.queryWorkplan.date+" "+e.arrivalMinute+":00",vehicleId:e.vehicleId,starttag:e.starttag,routeName:this.routeName};this.$refs.workplanLocus.query(t)},deleteData:function(e,t){"add"==t.model?this.departureData.splice(e,1):this._getRunningVehicle()},editWorkplan:function(e){e.model="edit",e.isEdit=!0},addTrip:function(e){if(null!=this.queryWorkplan.date&&""!=this.queryWorkplan.date){var t={selfNum:"",reasonType:"",reasonDetail:"",driverId:this.queryWorkplan.driverId,driverName:"",vehicleId:this.queryWorkplan.vehicleId,scheduleMinute:"",direction:"",starttag:"0",createNewGps:"1",trip:"",depHour:"",depMinute:"",arrHour:"",arrMinute:"",model:"add",isEdit:!0};this.departureData.splice(e+1,0,t)}else this.$message.error("请先选择日期")},editRow:function(e,t,i,r){e.isEdit=!0},cancelPlanSchedule:function(e){var t=this;this.$confirm("此操作将永久排班, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){var i={id:t.departureData[e].id};Object(n.e)(i).then(function(e){e.code==o.b?t._getRunningVehicle():t.$message.error(e.define)})}).catch(function(){t.$message({type:"info",message:"已取消删除"})})},saveWorkplan:function(e){var t=this,i=this.departureData.findIndex(function(e){return 1==e.isEdit});if(-1==i)return this.$message.info("没有正在编辑的趟次");var r=this.departureData[i],a={reasonType:r.reasonType,reasonDetail:r.reasonDetail,associationWorkplan:{id:r.id,dateString:this.queryWorkplan.date,departureString:r.departureMinute+":00",arrivalString:r.arrivalMinute+":00",routeName:this.routeName,starttag:r.starttag,driverId:r.driverId,vehicleId:r.vehicleId,createNewGps:r.createNewGps}};Object(n.c)(a).then(function(e){e.code==o.b?t._getRunningVehicle():t.$message.error(e.define)})},deleteCreateGps:function(e){var t=sessionStorage.getItem("workplan.gpsSign");if(t){var i=this.$loading({lock:!0,text:"Loading",spinner:"el-icon-loading",background:"rgba(0, 0, 0, 0.7)"});cancelWorkplanGps({date:e,sign:t}).then(function(e){e.code,o.b,i.close()})}},departureMinuteChange:function(e,t){var i="0"==e.starttag?this.lineRunTime.up:this.lineRunTime.down,r=t[0];if(r&&-1!=r.indexOf(":")){var a=r.split(":"),n=parseInt(a[0]),o=parseInt(a[1])+i;n+=parseInt(o/60),o%=60,n=parseInt(n)<10?"0"+parseInt(n):n,o=parseInt(o)<10?"0"+parseInt(o):o,this.lineRunTime.arrival=n+":"+o}}}},d={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"wrap-map"},[i("el-row",{staticStyle:{"margin-bottom":"15px"},attrs:{gutter:20}},[i("el-col",{attrs:{span:4}},[i("el-date-picker",{attrs:{size:"mini",type:"date","value-format":"yyyy-MM-dd",placeholder:"选择日期"},model:{value:e.queryWorkplan.date,callback:function(t){e.$set(e.queryWorkplan,"date",t)},expression:"queryWorkplan.date"}})],1),e._v(" "),i("el-col",{attrs:{span:3}},[i("el-select",{attrs:{filterable:"",size:"mini",placeholder:"请选择线路"},on:{change:e.routeChange},model:{value:e.queryWorkplan.routeName,callback:function(t){e.$set(e.queryWorkplan,"routeName",t)},expression:"queryWorkplan.routeName"}},e._l(e.routeList,function(e){return i("el-option",{key:e,attrs:{label:e,value:e}})}))],1),e._v(" "),i("el-col",{attrs:{span:3}},[i("el-checkbox",{model:{value:e.isQueryAll,callback:function(t){e.isQueryAll=t},expression:"isQueryAll"}},[e._v("是否查询所有车辆和司机")])],1),e._v(" "),i("el-col",{attrs:{span:3}},[i("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择驾驶员",size:"mini"},model:{value:e.queryWorkplan.driverId,callback:function(t){e.$set(e.queryWorkplan,"driverId",t)},expression:"queryWorkplan.driverId"}},e._l(e.drivers,function(e){return i("el-option",{key:e.driverId,attrs:{label:e.driverName,value:e.driverId}})}))],1),e._v(" "),i("el-col",{attrs:{span:3}},[i("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择车辆",size:"mini"},model:{value:e.queryWorkplan.vehicleId,callback:function(t){e.$set(e.queryWorkplan,"vehicleId",t)},expression:"queryWorkplan.vehicleId"}},e._l(e.vehicles,function(e){return i("el-option",{key:e.vehicleId,attrs:{label:e.selfNum,value:e.vehicleId}})}))],1),e._v(" "),i("el-col",{attrs:{span:2,offset:1}},[i("el-button",{attrs:{size:"mini",type:"primary"},on:{click:e.queryNotArrive}},[e._v("查询")])],1),e._v(" "),i("el-col",{attrs:{span:2,offset:1}},[i("el-button",{attrs:{size:"mini",type:"primary"},on:{click:e.addTrip}},[e._v("添加自定义趟次")])],1)],1),e._v(" "),i("p",[i("span",[e._v("上行运行时长：")]),i("span",{domProps:{textContent:e._s(e.lineRunTime.up)}}),e._v(" "),i("span",[e._v("下行运行时长：")]),i("span",{domProps:{textContent:e._s(e.lineRunTime.down)}}),e._v(" "),i("span",[e._v("到达时间：")]),i("span",{domProps:{textContent:e._s(e.lineRunTime.arrival)}})]),e._v(" "),i("el-row",[i("el-col",{attrs:{span:13}},[i("el-table",{staticStyle:{width:"100%"},attrs:{data:e.departureData,border:"",size:"mini","max-height":e.height,id:"departureData",height:e.height,align:"center"},on:{"cell-dblclick":e.editRow,"selection-change":e.selectRow}},[i("el-table-column",{attrs:{align:"center","header-align":"center",prop:"driverName",label:"司机姓名",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.isEdit?e._e():i("span",{domProps:{textContent:e._s(t.row.driverName)}}),e._v(" "),t.row.isEdit?i("el-select",{attrs:{filterable:"",placeholder:"请选择",size:"mini"},model:{value:t.row.driverId,callback:function(i){e.$set(t.row,"driverId",i)},expression:"scope.row.driverId"}},e._l(e.drivers,function(e){return i("el-option",{key:e.driverId,attrs:{label:e.driverName,value:e.driverId}})})):e._e()]}}])}),e._v(" "),i("el-table-column",{attrs:{align:"center","header-align":"center",prop:"selfNum",label:"车号",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.isEdit?e._e():i("span",{domProps:{textContent:e._s(t.row.selfNum)}}),e._v(" "),t.row.isEdit?i("el-select",{attrs:{filterable:"",placeholder:"请选择",size:"mini"},model:{value:t.row.vehicleId,callback:function(i){e.$set(t.row,"vehicleId",i)},expression:"scope.row.vehicleId"}},e._l(e.vehicles,function(e){return i("el-option",{key:e.vehicleId,attrs:{label:e.selfNum,value:e.vehicleId}})})):e._e()]}}])}),e._v(" "),i("el-table-column",{attrs:{align:"center","header-align":"center",prop:"direction",label:"方向",width:"90"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.isEdit?e._e():i("span",{domProps:{textContent:e._s(t.row.direction)}}),e._v(" "),t.row.isEdit?i("el-select",{attrs:{placeholder:"请选择",size:"mini"},model:{value:t.row.starttag,callback:function(i){e.$set(t.row,"starttag",i)},expression:"scope.row.starttag"}},[i("el-option",{attrs:{label:"上行",value:"0"}}),e._v(" "),i("el-option",{attrs:{label:"下行",value:"1"}})],1):e._e()]}}])}),e._v(" "),i("el-table-column",{attrs:{align:"center","header-align":"center",prop:"createNewGps",label:"重建GPS",width:"90"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.isEdit?e._e():i("span",{domProps:{textContent:e._s(t.row.createNewGps)}}),e._v(" "),t.row.isEdit?i("el-select",{attrs:{placeholder:"请选择",size:"mini"},model:{value:t.row.createNewGps,callback:function(i){e.$set(t.row,"createNewGps",i)},expression:"scope.row.createNewGps"}},[i("el-option",{attrs:{label:"不重新生成",value:"0"}}),e._v(" "),i("el-option",{attrs:{label:"重新生成",value:"1"}})],1):e._e()]}}])}),e._v(" "),i("el-table-column",{attrs:{align:"center","header-align":"center",prop:"departureMinute",label:"发车时间",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.isEdit?e._e():i("span",{domProps:{textContent:e._s(t.row.departureMinute)}}),e._v(" "),t.row.isEdit?i("timeInputComponent",{attrs:{width:"40",time:t.row.departureMinute},on:{time:function(i){e.departureMinuteChange(t.row,arguments)}},model:{value:t.row.departureMinute,callback:function(i){e.$set(t.row,"departureMinute",i)},expression:"scope.row.departureMinute"}}):e._e()]}}])}),e._v(" "),i("el-table-column",{attrs:{align:"center","header-align":"center",prop:"arrivalMinute",label:"到达时间",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.isEdit?e._e():i("span",{domProps:{textContent:e._s(t.row.arrivalMinute)}}),e._v(" "),t.row.isEdit?i("timeInputComponent",{attrs:{width:"40",time:t.row.arrivalMinute},model:{value:t.row.arrivalMinute,callback:function(i){e.$set(t.row,"arrivalMinute",i)},expression:"scope.row.arrivalMinute"}}):e._e()]}}])}),e._v(" "),i("el-table-column",{attrs:{align:"center","header-align":"center",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.isEdit?i("el-button",{attrs:{type:"success",size:"mini"},on:{click:function(i){e.queryLocus(t.row)}}},[e._v("查询\n            ")]):e._e(),e._v(" "),t.row.isEdit?i("el-button",{attrs:{type:"info",size:"mini"},on:{click:function(i){e.deleteData(t.$index,t.row)}}},[e._v("\n              取消\n            ")]):e._e(),e._v(" "),i("el-button",{directives:[{name:"show",rawName:"v-show",value:!t.row.isEdit,expression:"!scope.row.isEdit"}],attrs:{type:"primary",size:"mini",icon:"el-icon-edit"},on:{click:function(i){e.editWorkplan(t.row)}}}),e._v(" "),i("el-button",{directives:[{name:"show",rawName:"v-show",value:!t.row.isEdit,expression:"!scope.row.isEdit"}],attrs:{type:"danger",size:"mini",icon:"el-icon-delete"},on:{click:function(i){e.cancelPlanSchedule(t.$index)}}})]}}])})],1)],1),e._v(" "),i("el-col",{attrs:{span:11}},[i("workplan-locus",{ref:"workplanLocus",on:{addWorkplan:e.saveWorkplan}})],1)],1)],1)},staticRenderFns:[]};var p=i("VU/8")(c,d,!1,function(e){i("y3EO")},"data-v-b4b1048a",null);t.default=p.exports}});
//# sourceMappingURL=1.6569eee35942c98ef999.js.map