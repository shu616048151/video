<%@ page import="edu.whut.imgProcess.auth.vo.AdminRedisVo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    AdminRedisVo adminRedisVo = (AdminRedisVo) request.getAttribute("admin");
    int userId = adminRedisVo.getId();
    String username = adminRedisVo.getUsername();
    String moduleJson = adminRedisVo.getModuleJson();
%>

<link type="text/css" rel="stylesheet" href="/jsp/common/css/nav.css">
<script src="/jsp/common/js/angular-md5.min.js"></script>
<script src="/jsp/common/js/angular-cookie.min.js"></script>
<script src="/jsp/common/js/sweetalert.min.js"></script>
<div ng-controller="nc">
    <!--顶部导航-->
    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <div id="topLeftId" class="navbar-header">
                <a class="navbar-brand" href="javascript:void(0)">
                    <%--<span><i class="icon-heart icon-large" style="color: red;"></i></span>--%>
                    <img src="/jsp/common/asset/img/logo.png" style="height: 38px;margin-top: -8px;" alt="logo"/>
                </a>
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse"
                        aria-expanded="false">
                    <span class="sr-only">响应式小屏幕切换导航</span>
                    <span class="glyphicon glyphicon-bar"></span>
                    <span class="glyphicon glyphicon-bar"></span>
                    <span class="glyphicon glyphicon-bar"></span>
                </button>
            </div>
            <%--<div id="topMidId" class="navbar-text text-center" ng-style="topNavObj.midStyle()"
                 style="margin-left:0;margin-right: 0;">
                <span style="font-size: 100%;word-break: keep-all;" class="text-primary">变购链管理平台</span>
            </div>值得保留，毕竟手动居中 --%>
            <div class="text-center" style="position: absolute;top: 15px;left: 0;width: 100%;">
                <span class="text-white" style="font-size: 120%;">图像处理云平台</span>
            </div>
            <div class="collapse navbar-collapse" id="navbar-collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li id="topRightId" class="dropdown">
                        <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown">
                            <span style="color: whitesmoke" ng-bind="username"></span>&nbsp;<b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a href="javascript:void(0)" class="p-y" ng-click="entity._openModal('editPwd')">
                                    <span class="icon-edit m-r"></span> 修改密码</a>
                            </li>
                            <li class="p-y">
                                <a href="javascript:void(0)" class="p-y" ng-click="logout()">
                                    <span class="icon-signout m-r"></span> 退出</a>
                            </li>
                            <!--<li class="divider"></li>-->
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <!--左侧导航-->
    <div class="sidebar" ng-class='{"collapse":collapse}'>
        <div id="sidebar-container">
            <ul class="nav nav-sidebar">
                <li>
                    <a href="javascript:void(0)" ng-click="collapseF()" class="text-center hover">
                        <span ng-class='{"icon-align-justify":!collapse, "icon-list":collapse}'></span>
                    </a>
                </li>
            </ul>
            <ul class="nav nav-sidebar" ng-repeat="nav in navs"
                ng-class='{"nav-line":nav.index==40||nav.index==60||nav.index==120}'><!--3条横线 border-top-->
                <li ng-class='{"active":index == nav.name && !nav.nodes.length}'>
                    <!--没有子列表-->
                    <a ng-if="!nav.nodes.length" ng-href="{{nav.url}}"
                       ng-mouseenter="nav.hover = true" ng-mouseleave="nav.hover = false">
                        <span class="{{nav.icon}}"></span>
                        <span class="m-l-lg" ng-bind="nav.name" ng-class='{"hide":collapse}'></span>
                    </a>
                    <!--有子列表-->
                    <a ng-if="nav.nodes.length" ng-click='collapseNav($index)'
                       ng-init="nav.in = index == nav.name" href="javascript:void(0)"
                       ng-mouseenter="nav.hover = true" ng-mouseleave="nav.hover = false">
                        <span ng-class='{"icon-chevron-down":nav.in, "icon-chevron-right":!nav.in}'></span>
                        <span class="m-l-lg" ng-bind="nav.name" ng-class='{"hide":collapse}'></span>
                    </a>
                    <!--tooltip-->
                    <div ng-show="nav.hover && collapse" class="nav-tooltip ng-hide" ng-bind="nav.name"></div>
                </li>
                <!--子列表-->
                <ul ng-if="nav.nodes.length" ng-class='{"collapse":nav.nodes.length,"in":nav.in}'
                    id='{{"collapse-"+nav.name}}' class="nav nav-sidebar-sub">
                    <li ng-repeat="sub in nav.nodes"
                        ng-class='{"active":nav.name == index && subIndex == sub.name}'>
                        <a ng-href="{{sub.url}}" ng-mouseenter="sub.hover = true" ng-mouseleave="sub.hover = false">
                            <span class="{{sub.icon}}"></span>
                            <span class="m-l-lg" ng-bind="sub.name" ng-class='{"hide":collapse}'></span>
                        </a>

                        <div ng-show="sub.hover && collapse" class="nav-tooltip ng-hide" ng-bind="sub.name"></div>
                    </li>
                </ul>
            </ul>
        </div>
    </div>
    <%--模态框--%>
    <!--修改密码-->
    <div entity-modal="modal-nav-editPwd" title="密码" e="entity">
        <entity-modal-body>
            <div entity-edit-text="oldPassword" type="password" title="旧密码" entity="entity.entity" e="entity"
                 vld="entity.validate"></div>
            <div entity-edit-text="password" type="password" title="新密码" entity="entity.entity" e="entity"
                 vld="entity.validate"></div>
            <div entity-edit-text="confirmPassword" type="password" title="确认密码" entity="entity.entity" e="entity"
                 vld="entity.validate" confirm="yes"></div>
        </entity-modal-body>
    </div>
    <!--消息提示-->
    <div entity-modal-hint="modal-nav-hint" title="提示" action="hint" size="sm">
        <entity-modal-hint-body>
            <span ng-bind="toastArr[0]"></span>
        </entity-modal-hint-body>
    </div>
    <!--删除确认-->
    <div entity-modal-hint="modal-nav-hint-del" title="提示" action="confirm" size="sm"
         submit="logoutAdmin()">
        <entity-modal-hint-body>
            <span ng-bind="toastArr[1]"></span>
        </entity-modal-hint-body>
    </div>
</div>
<script>
    var userId = "<%=userId%>";
    angular.module("nm", ["baseModule", "angular-md5", "ipCookie"])
        .controller("nc", function ($scope, $location, $rootScope, $interval, $timeout,
                                    ajax, md5, entity, ipCookie, alertService) {
            $scope.toastArr = ["消息提示!", "确认要退出登录吗?"];
            /*可以本地测试用
             $scope.navs = [
             {"id": 1, "name": "模块管理", "url": "/jsp/module/module.jsp", "icon": "icon-sitemap", "nodes": []},
             {"id": 2, "name": "角色管理", "url": "/jsp/role/role.jsp", "icon": "icon-group", "nodes": []},
             {"id": 3, "name": "账号管理", "url": "/jsp/account/account.jsp", "icon": "icon-lock", "nodes": []},
             {"id": 4, "name": "子账号管理", "url": "/jsp/account/accountSubs.jsp", "icon": "icon-user", "nodes": []},
             {"id": 5, "name": "系统设置", "url": "/jsp/config/config.jsp", "icon": "icon-cog", "nodes": []}];*/
            /*$scope.getModuleList = function () {
             ajax.ajax("/user/admin/getModuleList", "POST", {
             userId: 1
             }).success(function (data) {
             console.log(data);
             if(data.success){
             $scope.moduleList = data.list;
             $scope.navs = $scope.moduleList;
             }
             });
             };
             $scope.$watch('$viewContentLoaded', function () {
             $scope.getModuleList();
             });*/
            $scope.username = "<%=username%>";
            $scope.navs = <%=moduleJson%>;

            //点击左侧导航栏
            $scope.collapse = $location.search().collapse && $location.search().collapse == "true";
            if ($scope.collapse) {
                $("body").addClass("nav-collapse");
            }
            $scope.collapseF = function () {
                $scope.collapse = !$scope.collapse;
                $location.search("collapse", $scope.collapse ? "true" : "false");
                if ($scope.collapse) {
                    $("body").addClass("nav-collapse");
                } else {
                    $("body").removeClass("nav-collapse");
                }
            };
            $scope.collapseNav = function (index) {
                var nav = $scope.navs[index];
                nav.in = !nav.in;
                $("#collapse-" + nav.name).collapse(nav.in ? "show" : "hide");
            };

            var sideScroll = false;
            var preventDefault = function (e) {
                e.preventDefault();
            };
            $(".sidebar").on("mouseenter", function () {
                window.addEventListener('mousewheel', preventDefault);
            });
            $(".sidebar").on("mouseleave", function () {
                window.removeEventListener('mousewheel', preventDefault);
            });
            document.getElementById("sidebar-container").addEventListener("mousewheel", function (e) {
                if (sideScroll) {//等待动画完成
                    return;
                }
                var sidebar = $(".sidebar");
                var sidebarContainer = $("#sidebar-container");
                var floor = sidebar.height() - sidebarContainer.height();
                floor = floor > 0 ? 0 : floor;
                var topOrigin = sidebarContainer.css("margin-top");
                topOrigin = parseInt(topOrigin.substring(0, topOrigin.length - 2));//去掉单位px
                var top = topOrigin - e.deltaY;
                top = top > 0 ? 0 : top;
                top = top < floor ? floor : top;
                if (top != topOrigin) {
                    sideScroll = true;
                    sidebarContainer.animate({"marginTop": top}, "fast", "swing", function () {
                        sideScroll = false;
                    });
                }
            });
            /**顶部导航*/
            /*$scope.topMidWidth = function () {
             $("#topMidId").width(
             window.innerWidth - (Number($("#topLeftId").width()) + 15 * 2)//15*2因为有margin
             - $("#topRightId").width() - 4//4为了消除可能由于border有宽度而影响topMidId的宽度
             );
             };
             (function () {
             $interval(function () {
             $scope.topMidWidth();
             },200,10);
             $(window).resize(function () {
             $scope.topMidWidth();
             });
             })();*/
            /**修改密码*/
            $scope.entity = entity.getEntity(
                {oldPassword: "", password: "", confirmPassword: ""},
                {
                    oldPassword: {}, password: {}, confirmPassword: {}
                }, function (action, row) {//beforeOpen
                    if ($scope.entity.action === "editPwd") {
                        $scope.moduleList = angular.copy($scope.moduleOriginList);
                    }
                }, function () {//submit
                    if ($scope.entity.action === "editPwd") {
                        ajax.ajax("/user/admin/updateAdminPwd", "POST", {
                            userId: userId,
                            id: userId,
                            oldPwd: md5.createHash($scope.entity.entity.oldPassword),
                            newPwd: md5.createHash($scope.entity.entity.password)
                        }).success(function (data) {
                            console.log(data);
                            if (data.success) {
                                alert("修改成功,请重新登录");
                                $scope.logoutAdmin();
                            } else {
                                alert("修改密码失败");
                            }
                        }).error(function (data) {
                            console.log(data);
                        });
                    }
                    $scope.entity._success();//隐藏模态框
                }, "modal-nav-editPwd");
            /**退出登录*/
            $scope.logout = function () {
                //$("#modal-nav-hint-del").modal("show");
                //alertService.show("操作成功!", "success", "80%");
                //swal("Good job!", "You clicked the button!", "info");//error warning info success
                swal("确认要退出登录吗?", {
                    buttons: {
                        cancel: "关闭",
                        catch: {
                            text: "确定",
                            value: "catch"
                        }
                    }
                }).then(function (value) {
                    switch (value) {
                        case "catch":
                            //swal("提示!", "操作成功!", "success");
                            $scope.logoutAdmin();
                            break;
                        default:
                        //swal("提示!", "操作失败!", "error");
                    }
                });
            };
            $scope.logoutAdmin = function () {
                ajax.ajax("/user/login/logoutAdmin", "POST", {
                    userId: userId,
                    token: ipCookie("token")
                }).success(function (data) {
                    ipCookie.remove("token");
                    var protocolStr = document.location.protocol,
                        hostname = document.location.hostname,
                        urls = ["http://"+hostname+":80/", "http://"+hostname+":80/"];
                    if (protocolStr == "http:") {
                        window.location.href = urls[0];
                    } else if (protocolStr == "https:") {
                        window.location.href = urls[1];
                    }
                });
            };
        });
</script>
