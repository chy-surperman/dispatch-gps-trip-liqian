webpackJsonp([12],{P9yX:function(t,e){},Sme4:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=a("02xr"),n=a("uxtY"),r=a("OMN4"),o=a.n(r),s=(a("+v9d"),{name:"vehicleTripDetail",data:function(){return{routeList:[],routeName:"",workdate:"",plateNum:"",statisticsType:"1",timeTables:[],showMileageDialog:!1,mileageData:{},days:[],downPagination:{currentPage:1,pageSizes:[10,20,30,40],pageSize:10,total:40}}},methods:{_getRouteLis:function(){var t=this;Object(i.r)().then(function(e){e.code==n.b&&(t.routeList=e.result,t.routeName=t.routeList[0])})},queryTripDetail:function(){var t=this,e={workdate:this.workdate,routeName:this.routeName,statisticsType:this.statisticsType,page:this.downPagination.currentPage,pageSize:this.downPagination.pageSize};Object(i.v)(e).then(function(e){e.code==n.b&&(t.timeTables=e.result.dataList,t.downPagination.total=e.result.totalCount)})},downHandleSizeChange:function(t){this.downPagination.pageSize=t,this.queryTripDetail()},downHandleCurrentChange:function(t){this.downPagination.currentPage=t,this.queryTripDetail()},exportData:function(){var t=this,e=n.c+"/report/down/vehicle?routeName="+this.routeName+"&workdate="+this.workdate+"&statisticsType="+this.statisticsType;o()({method:"get",url:e,responseType:"blob"}).then(function(e){if(e){var a=window.URL.createObjectURL(e.data),i=document.createElement("a");i.style.display="none",i.href=a;var n=t.routeName+"_车辆趟次统计.xlsx";i.setAttribute("download",n),document.body.appendChild(i),i.click()}})},dateChange:function(t){this.createDay()},routeChange:function(t){},createDay:function(){var t=this.workdate.split("-"),e=new Date(parseInt(t[0]),parseInt(t[1]),0).getDate(),a=new Date(parseInt(t[0]),parseInt(t[1])-1,1),i=[];i.push({id:-2,dayStr:"selfNum",width:70,day:"车号"});for(var n=0;n<e;n++){var r=a.getDate()+n,o={id:n,width:40,dayStr:"v"+r,day:r+""};i.push(o)}i.push({id:-2,dayStr:"tripCount",width:70,day:"合计"});i.push({id:-3,dayStr:"standTripMileage",width:70,day:"趟公里"});i.push({id:-4,dayStr:"totalMileage",width:80,day:"班次公里数"});i.push({id:-4,dayStr:"gpsMileageTotal",width:80,day:"GPS公里数"}),this.days=i,this.timeTables=[]}},created:function(){this._getRouteLis()}}),l={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"schedule-wrap"},[a("el-row",{staticClass:"schedule-panel"},[a("el-row",{attrs:{gutter:20}},[a("el-col",{attrs:{span:3}},[a("el-select",{attrs:{size:"mini",placeholder:"请选择线路"},on:{change:t.routeChange},model:{value:t.routeName,callback:function(e){t.routeName=e},expression:"routeName"}},t._l(t.routeList,function(t){return a("el-option",{key:t,attrs:{label:t,value:t}})}))],1),t._v(" "),a("el-col",{attrs:{span:4}},[a("el-date-picker",{attrs:{size:"mini",type:"month","value-format":"yyyy-MM",placeholder:"选择月份"},on:{change:t.dateChange},model:{value:t.workdate,callback:function(e){t.workdate=e},expression:"workdate"}})],1),t._v(" "),a("el-col",{attrs:{span:2}},[a("el-button",{attrs:{type:"primary",size:"mini"},on:{click:t.queryTripDetail}},[t._v("查询")])],1),t._v(" "),a("el-col",{attrs:{span:1}},[a("el-button",{attrs:{type:"primary",size:"mini"},on:{click:t.exportData}},[t._v("导出")])],1)],1),t._v(" "),a("el-table",{staticStyle:{width:"100%"},attrs:{"max-height":"500",size:"medium",data:t.timeTables,align:"center","highlight-current-row":""}},t._l(t.days,function(t){return a("el-table-column",{key:t.id,attrs:{prop:t.dayStr,label:t.day,width:t.width}})}))],1),t._v(" "),a("el-row",{staticStyle:{"margin-top":"20px"},attrs:{gutter:20}},[a("el-col",{attrs:{span:10,offset:12}},[a("el-pagination",{attrs:{"current-page":t.downPagination.currentPage,"page-sizes":t.downPagination.pageSizes,"page-size":t.downPagination.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:t.downPagination.total},on:{"size-change":t.downHandleSizeChange,"current-change":t.downHandleCurrentChange}})],1)],1)],1)},staticRenderFns:[]};var d=a("VU/8")(s,l,!1,function(t){a("P9yX")},"data-v-505325ba",null);e.default=d.exports}});
//# sourceMappingURL=12.05146e960ce817e7578f.js.map