webpackJsonp([8],{rOup:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=a("Dd8w"),i=a.n(r),l=a("02xr");Math.PI;var o=a("OMN4"),n=a.n(o),s=a("uxtY"),u=a("/IwO"),m=new u.AMapManager,c={name:"locusMap",data:function(){return{wrapperHeight:650,fullscreenLoading:!1,queryForm:{routeName:"",starttag:""},inputForm:{longitude:"",latitude:""},routeList:[],zoom:12,center:[112.998758,28.182349],amapManager:m,events:{init:this.initPathSimplifierIns},map:{},markerList:[],amapMarkerList:[],reportSiteMark:{}}},created:function(){this.getRouteNames()},mounted:function(){var e=window.innerHeight||document.documentElement.clientHeight||document.body.clientHeight;this.wrapperHeight=e-62},methods:{_getRouteMsg:function(){var e=this;Object(l.r)().then(function(t){t.code==s.b&&(e.routeList=t.result,e.queryForm.routeName=e.routeList[0])})},initPathSimplifierIns:function(e){this.map=e,this.map.on("click",this.mapClick);var t=this;AMapUI.loadUI(["overlay/SimpleMarker"],function(a){t.reportSiteMark=new a({iconLabel:{innerHTML:"<i>报站</i>",style:{color:"#fff"}},iconStyle:"red",map:e})})},mapClick:function(e){e.lnglat},addMark:function(){},elementMarkHandler:function(e){var t=[];e.forEach(function(e){t.push({position:[e.longitude,e.latitude],events:{click:function(){alert(e.siteName)}},visible:!0})}),this.markerList=t},amapMarkerHandler:function(e){var t=this;e.forEach(function(e){var a=[e.longitude,e.latitude],r=new AMap.Marker({position:new AMap.LngLat(a[0],a[1]),title:"北京"});r.setLabel({offset:new AMap.Pixel(20,20),content:"<div class='info'>"+e.siteName+"</div>",direction:"right"}),0,t.map.add(r)}),this.reportSiteMark.setPosition([113.135691,28.175375])},query:function(){var e=this,t=i()({},this.queryForm);this.map.remove(this.markerList),n.a.get("http://124.71.35.135:8282/device/gps/site/route",{params:t}).then(function(t){e.amapMarkerHandler(t.data.result)})},getRouteNames:function(){var e=this;n.a.get("http://124.71.35.135:8282/device/base/data/routeNames",{}).then(function(t){console.log(t),e.routeList=t.data.result,e.queryForm.routeName=e.routeList[0]})}}},p={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"wrap-map"},[a("div",{staticClass:"amap-page-container"},[a("el-amap",{staticClass:"amap-demo",style:{height:e.wrapperHeight+"px"},attrs:{vid:"amapDemo",center:e.center,"map-manager":e.amapManager,zoom:e.zoom,events:e.events}},e._l(e.markerList,function(e,t){return a("el-amap-marker",{key:t,attrs:{position:e.position,visible:e.visible,events:e.events,vid:t}})}))],1),e._v(" "),a("div",{staticClass:"query-form-panel"},[a("el-collapse",[a("el-collapse-item",{attrs:{title:"查询面版"}},[a("el-form",{ref:"form",staticClass:"query-form-box",attrs:{model:e.queryForm,"label-width":"80px"}},[a("el-form-item",{attrs:{label:"线路"}},[a("el-col",{attrs:{span:20}},[a("el-select",{attrs:{filterable:"",size:"medium",placeholder:"请选择线路"},model:{value:e.queryForm.routeName,callback:function(t){e.$set(e.queryForm,"routeName",t)},expression:"queryForm.routeName"}},e._l(e.routeList,function(e){return a("el-option",{key:e,attrs:{label:e,value:e}})}))],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"方向"}},[a("el-col",{attrs:{span:20}},[a("el-select",{attrs:{filterabl:"",size:"medium",placeholder:"请选择线路"},model:{value:e.queryForm.starttag,callback:function(t){e.$set(e.queryForm,"starttag",t)},expression:"queryForm.starttag"}},[a("el-option",{attrs:{label:"上行",value:"0"}}),e._v(" "),a("el-option",{attrs:{label:"下行",value:"1"}})],1)],1)],1)],1),e._v(" "),a("el-row",[a("el-col",[a("el-button",{attrs:{type:"primary",size:"small"},on:{click:e.query}},[e._v("查询")])],1)],1)],1)],1)],1),e._v(" "),a("div",{staticClass:"mark-form-panel"},[a("el-collapse",[a("el-collapse-item",{attrs:{title:"mark面版"}},[a("el-form",{ref:"form",staticClass:"query-form-box",attrs:{model:e.inputForm,"label-width":"80px"}},[a("el-form-item",{attrs:{label:"经度"}},[a("el-col",{attrs:{span:20}},[a("el-input",{attrs:{placeholder:"请输入内容",clearable:""},model:{value:e.inputForm.longitude,callback:function(t){e.$set(e.inputForm,"longitude",t)},expression:"inputForm.longitude"}})],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"纬度"}},[a("el-col",{attrs:{span:20}},[a("el-input",{attrs:{placeholder:"请输入内容",clearable:""},model:{value:e.inputForm.latitude,callback:function(t){e.$set(e.inputForm,"latitude",t)},expression:"inputForm.latitude"}})],1)],1)],1),e._v(" "),a("el-row",[a("el-col",[a("el-button",{attrs:{type:"primary",size:"small"},on:{click:e.addMark}},[e._v("添加点")])],1)],1)],1)],1)],1)])},staticRenderFns:[]};var d=a("VU/8")(c,p,!1,function(e){a("uCvP")},"data-v-7815e672",null);t.default=d.exports},uCvP:function(e,t){}});
//# sourceMappingURL=8.39d6beec552378a0c80b.js.map