/**
 * 根据d9lab实验室前辈们的经验 总结出一份基础模块类
 * 简化之前繁琐的依赖注入,优化其内部实现代码,并适当扩展其功能
 * --2017 12 17 17:36 zouy
 */
(function (angular, $) {
    "use strict";

    /*var config = {
     data: {},
     getData: function () {
     /!**读config.json*!/
     $.getJSON("/jsp/common/template/config.json", function (data) {
     config.config = data;
     });
     }
     };*/

    /**过滤器*/
    angular.module("filterModule", [])
        .filter('trustContent', ['$sce', function ($sce) {//强制将html字符串转化为html语句
            return function (text) {
                return $sce.trustAsHtml(text);
            };
        }])
        .filter('mergerArea', function () {
            return function (mergerArea) {
                return mergerArea ? (mergerArea == "中国" ? mergerArea : mergerArea.replace(/,|中国/g, "")) : "";
            }
        })
        //ng-bind="time | date : 'yyyy-MM-dd HH:mm'"//直接在绑定时使用filter表达式
        .filter('fmtDateYMdHMcn', ['$filter', function ($filter) {
            return function (time) {
                return time ? $filter('date')(time, 'yyyy年MM月dd日 HH:mm') : "";
            }
        }])
        .filter('fmtDateYMdcn', ['$filter', function ($filter) {
            return function (time) {
                return time ? $filter('date')(time, 'yyyy年MM月dd日') : "";
            }
        }])
        .filter('fmtDateYMDHMsub', ['$filter', function ($filter) {
            return function (time) {
                return time ? $filter('date')(time, 'yyyy-MM-dd HH:mm') : "";
            }
        }])
        .filter('fmtDateYMDHMSsub', ['$filter', function ($filter) {
            return function (time) {
                return time ? $filter('date')(time, 'yyyy-MM-dd HH:mm:ss') : "";
            }
        }]);
    /**ajax请求*/
    angular.module("ajaxModule", [])
        .config(["$httpProvider", function ($httpProvider) {
            // Use x-www-form-urlencoded Content-Type
            $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
            $httpProvider.defaults.withCredentials = true;//配合跨域
            /**
             * The workhorse; converts an object to x-www-form-urlencoded serialization.
             * @param {Object} obj
             * @return {String}
             */
            var param = function (obj) {
                var query = '', name, value, fullSubName, subName, subValue, innerObj, i;
                for (name in obj) {
                    value = obj[name];
                    if (value instanceof Array) {
                        for (i = 0; i < value.length; ++i) {
                            subValue = value[i];
                            fullSubName = name;
                            innerObj = {};
                            innerObj[fullSubName] = subValue;
                            query += param(innerObj) + '&';
                        }
                    }
                    else if (value instanceof Object) {
                        for (subName in value) {
                            subValue = value[subName];
                            fullSubName = name + '[' + subName + ']';
                            innerObj = {};
                            innerObj[fullSubName] = subValue;
                            query += param(innerObj) + '&';
                        }
                    }
                    else if (value !== undefined && value !== null)
                        query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
                }

                return query.length ? query.substr(0, query.length - 1) : query;
            };
            // Override $http service's default transformRequest
            $httpProvider.defaults.transformRequest = [function (data) {
                return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
            }];
        }])
        .service("ajax", function ($http) {
            this.ajax = function (url, method, params) {
                var config = {
                    url: url,
                    method: method
                };
                if (method === "GET") {
                    config.params = params;
                } else {
                    config.data = params;
                }
                return $http(config);
            };
        });
    /**表单元素,模态框*/
    angular.module("entityModule", ["ngFileUpload"])
        .service("entity", function () {
            this.getEntity = function (entity, validate, beforeOpen, submit, modalId) {
                var _entity = {
                    entity: entity,
                    validate: validate,
                    submit: submit,
                    modalId: modalId,
                    modalInitial: true,
                    _openModal: function (action, row) {
                        _entity.action = action;
                        _entity.modalInitial = true;
                        //不是编辑模态框就初始化_entity.entity
                        if (action === "add" || action === "addSub" || action === "editPwd") {
                            $.each(_entity.entity, function (k) {
                                _entity.entity[k] = "";
                            });
                        }
                        //用于提交
                        _entity.editEntity = angular.copy(row);
                        //初次打开模态框不触发表单验证
                        $.each(_entity.validate, function (k) {
                            _entity.validate[k].triggle = false;
                        });
                        //回调函数里打开模态框(暂时不用)
                        beforeOpen && beforeOpen(action, _entity.editEntity);
                        setTimeout(function () {
                            $("#" + _entity.modalId).modal("show");
                        }, 100);
                    },
                    _canSubmit: function () {
                        for (var i in _entity.validate) {
                            //这里的canSubmit是在input directive里定义的
                            if (!_entity.validate[i].canSubmit()) {
                                return false;
                            }
                        }
                        return true;
                    },
                    _submit: function () {
                        _entity.submit && _entity.submit();
                    },
                    _success: function () {
                        $("#" + _entity.modalId).modal("hide");
                    },
                    _error: function () {
                        $("#" + _entity.modalId).modal("hide");
                    }
                };
                return _entity;
            };
        })
        .directive("entityModal", function ($timeout) {
            return {
                restrict: 'AE',
                replace: true,
                transclude: {//在html中用E表述
                    "body": "entityModalBody",
                    "footer": "?entityModalFooter"//?表示可选
                },
                scope: {
                    modalId: "@entityModal", title: "@",
                    entity: "=e", size: "@"
                },
                templateUrl: "/jsp/common/template/modal_template.html",
                link: function (scope) {
                    if (scope.size === "" || scope.size === undefined ||
                        scope.size === null) {
                        scope.size = "lg";//默认lg 另有md sm 尺寸依次减小
                    }
                    scope.entity.modalTopAlertShow = false;
                    scope.submit = function () {
                        //点击模态框提交按钮强制触发表单验证
                        scope.entity.modalInitial = false;
                        $.each(scope.entity.validate, function (k) {
                            scope.entity.validate[k].triggle = true;
                        });
                        scope.entity.modalTopAlertShow = !scope.entity._canSubmit();
                        if (scope.entity.modalTopAlertShow) {//表单验证未通过
                            $timeout(function () {
                                scope.entity.modalTopAlertShow = false;
                            }, 1500);
                        } else {//提交表单
                            scope.entity._submit();
                        }
                    };
                    scope.modalBodyClick = function () {
                        //点击modal-body区域(包括表单元素)表示模态框开始使用
                        scope.entity.modalInitial = false;
                    };
                    scope.action = function () {
                        var title = "";
                        switch (scope.entity.action) {
                            case "add":
                                title = "新增 ";
                                break;
                            case "edit":
                                title = "编辑 ";
                                break;
                            case "addSub":
                                title = "新增 子";
                                break;
                            case "editPwd":
                                title = "修改 ";
                                break;
                        }
                        return title;
                    };
                }
            }
        })
        .directive("entityModalView", function () {
            return {
                restrict: 'AE',
                replace: true,
                transclude: {//在html中用E表述
                    "body": "entityModalViewBody",
                    "footer": "?entityModalViewFooter"//?表示可选
                },
                scope: {
                    modalId: "@entityModalView", title: "@",
                    entity: "=e", size: "@"
                },
                templateUrl: "/jsp/common/template/modal_template.html",
                link: function (scope) {
                    if (scope.size === "" || scope.size === undefined ||
                        scope.size === null) {
                        scope.size = "lg";//默认lg 另有md sm 尺寸依次减小
                    }
                }
            }
        })
        .directive("entityModalHint", function () {
            return {
                restrict: 'AE',
                replace: true,
                transclude: {//在html中用E表述
                    "body": "entityModalHintBody",
                    "footer": "?entityModalHintFooter"//?表示可选
                },
                scope: {
                    modalId: "@entityModalHint",
                    title: "@", action: "@", size: "@",
                    submit: "&"
                },
                templateUrl: "/jsp/common/template/modal_hint_template.html",
                link: function (scope) {
                    if (scope.size === "" || scope.size === undefined ||
                        scope.size === null) {
                        scope.size = "lg";//默认lg 另有md sm 尺寸依次减小
                    }
                }
            }
        })
        .directive("entityEditText", function () {
            /**
             * 一个directive只负责一个输入组件,所以对该输入组件的验证完全可以放在该directive中进行
             * 至于表单的提交,则与directive隔开,单独做判断
             */
            return {
                restrict: 'A',
                replace: true,
                scope: {
                    type: "@", name: "@entityEditText", title: "@", confirm: "@", rewidth: "@",
                    midHint: "@",
                    entity: "=", validate: "=vld", e: "=",
                    step: "=", min: "=", max: "=", maxlength: "="
                },
                template: '<div class="form-group">' +
                '<label class="col-sm-{{leftw}} col-sm-offset-{{offset}} control-label">' +
                '<span ng-if="isNeedValidate" class="icon-asterisk text-danger small" style="margin-right: 6px;"></span>' +
                '<span ng-bind="title"></span>' +
                '</label>' +
                '<div class="col-sm-{{rightw}}">' +
                '<input ng-class="{\'input-error\':inputError() && !e.modalInitial}" class="form-control" name="myForm"' +
                ' type="{{type}}" placeholder="请输入{{title}}" ng-model="entity[name]" ' +
                ' step="{{step}}" min="{{min}}" max="{{max}}" ng-focus="inputFocus()" ng-blur="inputBlur()"' +
                ' maxlength="{{maxlength}}">' +
                /*'<div style="margin-top: 8px;font-size: 80%;" class="text-muted pull-left">{{midHint}}</div>' +*/
                '<div ng-if="midHint" style="margin-top: 8px;font-size: 80%;" class="text-muted pull-right">' +
                '还可以输入&nbsp;{{maxlength-entity[name].length}}&nbsp;/&nbsp;{{maxlength}}&nbsp;个字符</div>' +
                '</div>' +
                '<p ng-if="isNeedValidate" class="col-sm-2 text-danger" style="padding-top: 6px;margin-bottom: 0;">' +
                '<span ng-show="validate[name].triggle && !e.modalInitial">' +
                '<span ng-show="ifEmpty() && type!==\'email\'">{{title}}不能为空</span>' +
                '<span ng-if="confirm !== \'yes\'" ng-show="type===\'password\' && !ifEmpty() && !ifPassword()">密码长度6到15位</span>' +
                '<span ng-if="confirm === \'yes\'"><span ng-show="type===\'password\' && !ifConfirmPassword() && !ifEmpty()">两次密码不一致</span></span>' +
                '<span ng-show="type===\'tel\' && !ifEmpty() && !ifTel()">电话应为11位合法数字</span>' +
                '<span ng-show="type===\'email\' && !ifEmail()">邮箱格式不正确</span>' +
                '</span>' +
                '</p>' +
                '</div>',
                link: function (scope) {
                    //宽度初始化
                    (function () {
                        if (scope.rewidth === null || scope.rewidth === undefined || scope.rewidth === "") {
                            scope.leftw = 2;
                            scope.offset = 2;
                            scope.rightw = 4;
                        } else {
                            var arr = scope.rewidth.split("-");
                            scope.leftw = arr[0];
                            scope.offset = arr[1];
                            scope.rightw = arr[2];
                        }
                    })();
                    scope.isNeedValidate = typeof scope.validate !== "undefined" && typeof scope.validate[scope.name] !== "undefined";
                    scope.inputFocus = function () {
                        if (scope.isNeedValidate) {
                            scope.validate[scope.name].triggle = false;
                        }
                    };
                    scope.inputBlur = function () {
                        if (scope.isNeedValidate) {
                            scope.validate[scope.name].triggle = true;
                        }
                    };
                    scope.inputError = function () {
                        if (!scope.isNeedValidate) {
                            return false;
                        }
                        if (!scope.validate[scope.name].triggle) {
                            return false;
                        }
                        if (scope.ifEmpty()) {
                            return true;
                        }

                        switch (scope.type) {
                            case "text":
                                break;
                            case "tel":
                                return !scope.ifTel();
                                break;
                            case "password":
                                return !scope.ifPassword();
                                break;
                            case "email":
                                return !scope.ifEmail();
                                break;
                        }
                    };
                    scope.ifEmpty = function () {
                        var t = scope.entity[scope.name];
                        return t === null || t === "" || typeof t === "undefined";
                    };
                    scope.ifTel = function () {
                        return /^1[34578]\d{9}$/g.test(scope.entity[scope.name]);
                    };
                    scope.ifPassword = function () {
                        return /^\S{6,15}$/g.test(scope.entity[scope.name]);
                    };
                    scope.ifEmail = function () {
                        return /^([0-9A-Za-z\-_\.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g.test(scope.entity[scope.name]);
                    };
                    scope.ifConfirmPassword = function () {
                        return scope.entity.password === scope.entity.confirmPassword;
                    };
                    if (scope.isNeedValidate) {//初次不触发验证,并赋值验证函数
                        scope.validate[scope.name].triggle = false;
                        scope.validate[scope.name].canSubmit = function () {
                            return !scope.inputError();
                        };
                    }
                }
            }
        })
        .directive("entityEditTextarea", function () {
            return {
                restrict: 'A',
                replace: true,
                scope: {
                    name: "@entityEditTextarea", title: "@", placeholder: "@", rewidth: "@",
                    midHint: "@",
                    entity: "=", validate: "=vld", e: "=", maxlength: "="
                },
                template: '<div class="form-group">' +
                '<label class="col-sm-{{leftw}} col-sm-offset-{{offset}} control-label">' +
                '<span ng-if="isNeedValidate" class="icon-asterisk text-danger small" style="margin-right: 6px;"></span>' +
                '<span ng-bind="title"></span>' +
                '</label>' +
                '<div class="col-sm-{{rightw}}">' +
                '<textarea rows="6" class="form-control" placeholder="请输入{{title}}" ng-model="entity[name]"' +
                ' ng-class="{\'input-error\':inputError() && !e.modalInitial}" style="resize: none;"' +
                ' ng-focus="inputFocus()" ng-blur="inputBlur()" maxlength="{{maxlength}}"></textarea>' +
                /*'<div style="margin-top: 8px;font-size: 80%;" class="text-muted pull-left">{{midHint}}</div>' +*/
                '<div ng-if="midHint" style="margin-top: 8px;font-size: 80%;" class="text-muted pull-right">' +
                '还可以输入&nbsp;{{maxlength-entity[name].length}}&nbsp;/&nbsp;{{maxlength}}&nbsp;个字符</div>' +
                '</div>' +
                '<p ng-if="isNeedValidate" class="col-sm-2 text-danger" style="padding-top: 6px;margin-bottom: 0;">' +
                '<span ng-show="validate[name].triggle && !e.modalInitial">' +
                '<span ng-show="ifEmpty()">{{title}}不能为空</span>' +
                '</span>' +
                '</p>' +
                '</div>',
                link: function (scope) {
                    //宽度初始化
                    (function () {
                        if (scope.rewidth === null || scope.rewidth === undefined || scope.rewidth === "") {
                            scope.leftw = 2;
                            scope.offset = 2;
                            scope.rightw = 4;
                        } else {
                            var arr = scope.rewidth.split("-");
                            scope.leftw = arr[0];
                            scope.offset = arr[1];
                            scope.rightw = arr[2];
                        }
                    })();
                    scope.isNeedValidate = typeof scope.validate !== "undefined" && typeof scope.validate[scope.name] !== "undefined";
                    scope.inputFocus = function () {
                        if (scope.isNeedValidate) {
                            scope.validate[scope.name].triggle = false;
                        }
                    };
                    scope.inputBlur = function () {
                        if (scope.isNeedValidate) {
                            scope.validate[scope.name].triggle = true;
                        }
                    };
                    scope.inputError = function () {
                        if (!scope.isNeedValidate) {
                            return false;
                        }
                        if (!scope.validate[scope.name].triggle) {
                            return false;
                        }
                        if (scope.ifEmpty()) {
                            return true;
                        }
                    };
                    scope.ifEmpty = function () {
                        var t = scope.entity[scope.name];
                        return t === null || t === "" || typeof t === "undefined";
                    };
                    if (scope.isNeedValidate) {//初次不触发验证,并赋值验证函数
                        scope.validate[scope.name].triggle = false;
                        scope.validate[scope.name].canSubmit = function () {
                            return !scope.inputError();
                        };
                    }
                }
            }
        })
        .directive("entityRightButton", function () {
            return {
                restrict: 'A',
                replace: true,
                scope: {
                    name: "@entityRightButton", clas: "@",//不能用class,应该是保留字
                    click: "&", rewidth: "@"
                },
                template: '<div class="form-group">' +
                '<label class="col-sm-{{rewidth}} col-sm-offset-{{offset}} control-label">' +
                '</label>' +
                '<div class="col-sm-{{rightw}}">' +
                '<button class="btn form-control btn-default btn-{{clas}}"' +
                ' ng-click="click()">{{name}}</button>' +
                '</div>' +
                '</div>',
                link: function (scope) {
                    //宽度初始化
                    (function () {
                        if (scope.rewidth === null || scope.rewidth === undefined || scope.rewidth === "") {
                            scope.leftw = 2;
                            scope.offset = 2;
                            scope.rightw = 4;
                        } else {
                            var arr = scope.rewidth.split("-");
                            scope.leftw = arr[0];
                            scope.offset = arr[1];
                            scope.rightw = arr[2];
                        }
                    })();
                }
            }
        })
        .directive("entityEditImg", function () {
            return {
                restrict: 'A',
                replace: true,
                scope: {
                    name: "@entityEditImg", title: "@", myid: "@", rewidth: "@",
                    entity: "=", validate: "=vld", e: "="
                },
                template: '<div class="form-group">' +
                '<label class="col-sm-{{leftw}} col-sm-offset-{{offset}} control-label">' +
                '<span ng-bind="title"></span>' +
                '</label>' +
                '<div class="col-sm-{{rightw}}">' +
                '<div class="btn btn-primary block" ngf-select ng-model="entity[name]"' +
                'ngf-pattern="\'.jpg,.png,.gif,.jpeg\'" ngf-accept="\'image/jpg,image/png,image/gif,image/jpeg\'"' +
                'ngf-max-size="20MB" style="width: 100px;">选择文件</div>' +
                '<img id="{{myid}}" class="img-responsive img-rounded"' +
                ' ng-src="{{src}}" style="max-height: 200px;margin-top: 10px;">' +
                '</div>' +
                '</div>',
                link: function (scope) {
                    //宽度初始化
                    (function () {
                        if (scope.rewidth === null || scope.rewidth === undefined || scope.rewidth === "") {
                            scope.leftw = 2;
                            scope.offset = 2;
                            scope.rightw = 4;
                        } else {
                            var arr = scope.rewidth.split("-");
                            scope.leftw = arr[0];
                            scope.offset = arr[1];
                            scope.rightw = arr[2];
                        }
                    })();
                    scope.$watch('entity[name]', function () {
                        if (scope.entity[scope.name] !== "undefined" &&
                            scope.entity[scope.name] !== null) {
                            if (typeof scope.entity[scope.name] === "object") {
                                //图片预览//scope.entity[scope.name]即是原生file对象
                                var url = window.URL || window.webkitURL || window.mozURL;
                                scope.src = url.createObjectURL(scope.entity[scope.name]);
                                scope.e.imgChange = true;
                            } else if (typeof scope.entity[scope.name] === "string") {
                                //图片预览
                                scope.src = scope.entity[scope.name];
                            }
                        }
                    });
                }
            }
        })//图片
        .directive("entityEditFile", function () {//任意类型文件
            return {
                restrict: 'A',
                replace: true,
                scope: {
                    name: "@entityEditFile", title: "@",
                    pattern: "@", accept: "@", size: "@", rewidth: "@",
                    entity: "=", validate: "=vld", e: "="
                },
                template: '<div class="form-group">' +
                '<label class="col-sm-{{leftw}} col-sm-offset-{{offset}} control-label">' +
                '<span ng-bind="title"></span>' +
                '</label>' +
                '<div class="col-sm-{{rightw}}">' +
                '<div class="btn btn-primary block" ngf-select ng-model="entity[name]"' +
                'ngf-pattern="\'{{pattern}}\'" ngf-accept="\'application/x-excel\'"' +
                'ngf-max-size="size" style="width: 100px;">选择文件</div>' +
                '</div>' +
                '<p ng-if="isNeedValidate" class="col-xs-4 m-t m-b-0 text-danger" ng-show="inputError()" ng-bind="validate[name].t"></p>' +
                '</div>',
                link: function (scope) {
                    //宽度初始化
                    (function () {
                        if (scope.rewidth === null || scope.rewidth === undefined || scope.rewidth === "") {
                            scope.leftw = 2;
                            scope.offset = 2;
                            scope.rightw = 4;
                        } else {
                            var arr = scope.rewidth.split("-");
                            scope.leftw = arr[0];
                            scope.offset = arr[1];
                            scope.rightw = arr[2];
                        }
                    })();
                    scope.$watch('entity[name]', function () {
                        if (scope.entity[scope.name] !== "undefined" &&
                            scope.entity[scope.name] !== null) {
                            if (typeof scope.entity[scope.name] === "object") {
                                //文件详情//scope.entity[scope.name]即是原生file对象
                                var url = window.URL || window.webkitURL || window.mozURL;
                                scope.src = url.createObjectURL(scope.entity[scope.name]);
                                scope.e.fileChange = true;
                            }
                        }
                        console.log(scope.e.fileChange);
                    });
                }
            }
        })
        .directive("entityViewText", function () {
            return {
                restrict: 'A',
                replace: true,
                scope: {
                    value: "=entityViewText",
                    title: "@",
                    icon: "@", rewidth: "@"
                },
                template: '<div class="m-t row">' +
                '<p class="col-sm-{{leftw}} text-muted">' +
                '<span class="m-r {{icon}}"></span><span ng-bind="title"></span>' +
                '</p>' +
                '<div class="col-sm-{{rightw}}">' +
                '<p ng-bind="value"></p>' +
                '</div>' +
                '</div>',
                link: function (scope) {
                    //宽度初始化
                    (function () {
                        if (scope.rewidth === null || scope.rewidth === undefined || scope.rewidth === "") {
                            scope.leftw = 2;
                            scope.offset = 2;
                            scope.rightw = 4;
                        } else {
                            var arr = scope.rewidth.split("-");
                            scope.leftw = arr[0];
                            scope.offset = arr[1];
                            scope.rightw = arr[2];
                        }
                    })();
                }
            }
        })
        .directive("entityViewImg", function () {
            return {
                restrict: 'A',
                replace: true,
                scope: {
                    value: "=entityViewImg",
                    title: "@",
                    icon: "@", rewidth: "@"
                },
                template: '<div class="m-t row">' +
                '<p class="col-sm-{{leftw}} text-muted">' +
                '<span class="m-r {{icon}}"></span><span ng-bind="title"></span>' +
                '</p>' +
                '<div class="col-sm-{{rightw}}">' +
                '<img class="img-rounded" ng-src="{{value}}"' +
                ' style="max-height: 200px" alt="img" />' +
                '</div>' +
                '</div>',
                link: function (scope, elements) {
                    //宽度初始化
                    (function () {
                        if (scope.rewidth === null || scope.rewidth === undefined || scope.rewidth === "") {
                            scope.leftw = 2;
                            scope.offset = 2;
                            scope.rightw = 4;
                        } else {
                            var arr = scope.rewidth.split("-");
                            scope.leftw = arr[0];
                            scope.offset = arr[1];
                            scope.rightw = arr[2];
                        }
                    })();
                    scope.$watch('value', function () {
                        if(scope.value === undefined|| scope.value === null){
                            var $img = elements.children().next().children();//找到img元素
                            $img.attr("src", "");
                        }
                    });
                }
            }
        })
        .directive("entityViewTextarea", function () {
            return {
                restrict: 'A',
                replace: true,
                scope: {
                    value: "=entityViewTextarea",
                    title: "@",
                    icon: "@"
                },
                template: '<div class="m-t clearfix">' +
                '<p class="text-muted">' +
                '<span class="m-r {{icon}}"></span><span ng-bind="title"></span>' +
                '</p>' +
                '<div class="panel panel-default">' +
                '<div class="panel-body" style="overflow:auto;max-height: 300px;" ng-bind="value"></div>' +
                '</div>' +
                '</div>'
            }
        })
        .directive("entityViewTag", function () {
            return {
                restrict: 'A',
                replace: true,
                scope: {
                    value: "=entityViewTag",
                    title: "@", icon: "@", type: "@", rewidth: "@"
                },
                template: '<div class="form-group">' +
                '<label class="col-sm-{{leftw}} col-sm-offset-{{offset}}  control-label">' +
                '<span ng-bind="title"></span>' +
                '</label>' +
                '<div class="col-sm-{{rightw}} ">' +
                '<input type="text" class="form-control"' +
                ' ng-if="type===\'text\'" ng-model="value" disabled style="background-color: white;"/>' +
                '<input type="password" class="form-control"' +
                ' ng-if="type===\'password\'" ng-model="value" disabled style="background-color: white;"/>' +
                '<textarea rows="5" class="form-control" ng-if="type===\'textarea\'" ng-model="value"' +
                ' disabled style="background-color: white;resize: none;"></textarea>' +
                '<img class="img-rounded" ng-src="{{value}}"' +
                ' ng-if="type===\'img\'" style="max-height: 200px" alt="img" />' +
                '<div ng-if="type===\'html\'" class="panel panel-default">' +
                '<div class="panel-body" style="overflow:auto;max-height: 300px;" ng-bind-html="value | trustContent"></div>' +
                '</div>' +
                '</div>' +
                '</div>',
                link: function (scope) {
                    //宽度初始化
                    (function () {
                        if (scope.rewidth === null || scope.rewidth === undefined || scope.rewidth === "") {
                            scope.leftw = 2;
                            scope.offset = 2;
                            scope.rightw = 4;
                        } else {
                            var arr = scope.rewidth.split("-");
                            scope.leftw = arr[0];
                            scope.offset = arr[1];
                            scope.rightw = arr[2];
                        }
                    })();
                }
            }
        });
    /**表格分页*/
    angular.module("pageModule", [])
        .service("page", function () {
            this.page = function (load, orderBy, asc) {
                var page = {
                    sizes: [10, 15, 30, 50],
                    size: 15,
                    jump: 1,
                    hasPrev: false,
                    hasNext: false,
                    current: 1,
                    total: 1,
                    count: 0,
                    orderBy: typeof orderBy == "undefined" ? "" : orderBy,
                    asc: typeof asc == "undefined" ? true : asc,
                    pages: [1],
                    list: [],
                    load: function (current, size, orderBy, asc) {
                        (function () {
                            var time = 0;
                            var interval = setInterval(function () {
                                time++;
                                $("[data-toggle='tooltip']").tooltip();//触发提示框
                                if($("[data-toggle='tooltip']").length > 0 || time > 10){
                                    clearInterval(interval);
                                }
                            }, 300);
                        })();
                        load && load(current, size, orderBy, asc);
                    },
                    refreshPage: function (data) {
                        if (typeof data.page == "undefined") {
                            page.list = data.list;
                            page.hasPrev = false;
                            page.hasNext = false;
                            page.current = 1;
                            page.total = 1;
                            page.count = null;
                            return;
                        }
                        page.list = data.page.list;
                        page.hasPrev = data.page.hasPrev;
                        page.hasNext = data.page.hasNext;
                        page.current = data.page.current;
                        page.total = data.page.total;
                        page.count = data.page.count;
                        page.pages = [];
                        //初始化页码按钮列表
                        for (var i = 1; i <= page.total; i++) {
                            if (i == 1 || i == page.total) {
                                page.pages.push(i);
                            } else if (page.current - i > 4) {
                                i = page.current - 4;
                                page.pages.push("... ");
                            } else if (i - page.current > 3) {
                                i = page.total - 1;
                                page.pages.push(" ...");
                            } else {
                                page.pages.push(i);
                            }
                        }
                    },
                    pushPage: function (data) {
                        if (typeof data.page == "undefined") {
                            page.list = page.list ? page.list.concat(data.list) : data.list;
                            page.hasPrev = false;
                            page.hasNext = false;
                            page.current = 1;
                            page.total = 1;
                            page.count = null;
                            return;
                        }
                        page.list = page.list ? page.list.concat(data.page.list) : data.page.list;
                        page.hasPrev = data.page.hasPrev;
                        page.hasNext = data.page.hasNext;
                        page.current = data.page.current;
                        page.total = data.page.total;
                        page.count = data.page.count;
                        page.pages = [];
                        //初始化页码按钮列表
                        for (var i = 1; i <= page.total; i++) {
                            if (i == 1 || i == page.total) {
                                page.pages.push(i);
                            } else if (page.current - i > 4) {
                                i = page.current - 4;
                                page.pages.push("... ");
                            } else if (i - page.current > 3) {
                                i = page.total - 1;
                                page.pages.push(" ...");
                            } else {
                                page.pages.push(i);
                            }
                        }
                    },
                    orderTo: function (orderBy) {
                        if (typeof orderBy == "undefined" || orderBy.length == 0) {
                            return;
                        }
                        if (orderBy == page.orderBy) {
                            page.asc = !page.asc;
                        } else {
                            page.asc = true;
                            page.orderBy = orderBy;
                        }
                        page.load(page.current, page.size, page.orderBy, page.asc);
                    },
                    clear: function () {
                        page.list.length = 0
                    },
                    prev: function () {
                        if (page.current > 1) {
                            page.load(page.current - 1, page.size, page.orderBy, page.asc);
                        }
                    },
                    next: function () {
                        if (page.current < page.total) {
                            page.load(page.current + 1, page.size, page.orderBy, page.asc);
                        }
                    },
                    pageTo: function (current) {
                        if (!isNaN(current) && current != page.current) {
                            page.load(current, page.size, page.orderBy, page.asc);
                        }
                    },
                    refresh: function () {
                        page.load(page.current, page.size, page.orderBy, page.asc);
                    },
                    refreshTo: function (current) {
                        page.load(current, page.size, page.orderBy, page.asc);
                    }
                };
                page.refreshPage({
                    page: {
                        hasPrev: false,
                        hasNext: false,
                        current: 1,
                        total: 1,
                        count: 0
                    }
                }, page.orderBy, page.asc);
                return page;
            };
        });
    /**list & listPage*/
    angular.module("listModule", ["pageModule", "ajaxModule"])
        .directive("list", function (page, ajax) {
            return {
                restrict: 'A',
                replace: false,
                scope: {
                    close: "&",
                    title: "@",
                    height: "@",
                    templateId: "@",
                    closeDefault: "@",
                    entity: "=list"
                },
                template: '<div>' +
                '<h4 class="text-center text-success"><span ng-bind="title"></span>' +
                '<span class="label label-success m-l-lg" ng-bind="entity.length"></span>' +
                '</h4>' +
                '<div class="list-group m-b-0" style="max-height: {{height}};overflow-y: auto">' +
                '<div ng-repeat="row in entity track by $index" class="list-group-item list-group-item-success">' +
                '<button type="button" class="close" ng-click="closeRow($index)">&times;</button>' +
                '<div ng-include="templateId" style="margin-right:2em">' +
                '</div></div></div></div>',
                link: function (scope) {
                    scope.closeRow = function (index) {
                        if (typeof scope.closeDefault == "undefined") {
                            scope.close({$index: index})
                        } else {
                            scope.entity.splice(index, 1);
                        }
                    }
                }
            }
        })
        .directive("listView", function (page, ajax) {
            return {
                restrict: 'A',
                replace: false,
                scope: {
                    height: "@",
                    templateId: "@",
                    title: "@",
                    icon: "@",
                    entity: "=listView"
                },
                template: '<div class="m-t clearfix">' +
                '<p class="text-muted">' +
                '<span class="{{icon}} m-r"></span><span ng-bind="title"></span>' +
                ' </p>' +
                '<div class="list-group m-b-0" style="max-height: {{height}};overflow-y: auto">' +
                '<div ng-repeat="row in entity track by $index" class="list-group-item">' +
                '<div ng-include="templateId" ">' +
                '</div></div></div></div>'
            }
        })
        .directive("listPage", function (page, ajax) {
            return {
                restrict: 'A',
                replace: false,
                scope: {
                    service: "&",
                    select: "&",
                    selectDefault: "@",
                    placeholder: "@placeholder",
                    height: "@",
                    load: "&",
                    templateId: "@",
                    entity: "=listPage"
                },
                template: '<div>' +
                '<form class="m-b form-inline" ng-submit="page.refreshTo(1)">' +
                '<input class="form-control" ng-model="name" placeholder="{{placeholder}}">' +
                '<button class="btn btn-primary m-l" type="submit">搜索</button>' +
                '</form>' +
                '<div class="list-group m-b-0" style="overflow-y: auto;max-height: {{height}}">' +
                '<a href="javascript:void(0)" ng-repeat="row in page.list" ng-click="selectRow(row)" class="list-group-item list-group-item-info" ng-mouseenter="row.$hover = true" ng-mouseleave="row.$hover = false">' +
                '<p class="m-b-0 pull-left">' +
                '<span class="icon-arrow-left" ng-show="row.$hover"></span>' +
                '</p>' +
                '<div ng-include="templateId" style="margin-left: 2em"></div>' +
                '</a></div></div>',
                link: function (scope, element, attr) {
                    scope.listScroll = false;
                    $(element).find(".list-group").scroll(function () {
                        if (scope.listScroll) {
                            return;
                        }
                        var viewH = $(this).height();
                        var contentH = $(this).get(0).scrollHeight;
                        var scrollTop = $(this).scrollTop();
                        if (contentH - viewH - scrollTop <= 20) { //到达底部100px时,加载新内容
                            scope.listScroll = true;
                            scope.page.next();
                        }
                    });
                    scope.name = "";
                    scope.callback = function (data) {
                        if (data.success) {
                            if (scope.listScroll) {
                                scope.page.pushPage(data);
                            } else {
                                scope.page.refreshPage(data);
                            }
                            scope.listScroll = false;
                        }
                    };
                    scope.selectRow = function (row) {
                        if (typeof scope.selectDefault == "undefined") {
                            scope.select({$row: row});
                        } else {
                            if (scope.selectDefault.length > 0) {
                                for (var i in scope.entity) {
                                    if (scope.entity[i][scope.selectDefault] == row[scope.selectDefault]) {
                                        return;
                                    }
                                }
                            }
                            scope.entity.push(deepCopy(row, {}));
                        }
                    };
                    scope.loadPage = function (current, size) {
                        scope.load({$current: current, $size: size, $name: scope.name, $callback: scope.callback});
                    };
                    scope.page = page.page(scope.loadPage);
                    scope.service({$page: scope.page});
                    scope.page.refreshTo(1);
                }
            }
        });
    /**省市区*/
    angular.module("areaModule", ["ajaxModule"])
        .service("area", function (ajax) {
            this.getArea = function (onSelect, areaCode) {
                var url = "/server/area/getByParentCode";
                var method = "POST";
                var initAreaCode = typeof areaCode == "undefined" || areaCode == 100000 ? null : areaCode;
                var defaultArea = {code: 100000, lng: 116.3683244, lat: 39.915085, level: 0};
                var area = {
                    area: defaultArea,
                    provinces: [],
                    province: null,
                    cities: [],
                    city: null,
                    districts: [],
                    district: null,
                    onSelect: onSelect,
                    reset: function (areaCode) {
                        initAreaCode = typeof areaCode == "undefined" ? null : areaCode;
                        area.getProvinces(true);
                    },
                    getProvinces: function (isInit) {
                        area.area = defaultArea;
                        area.citys = [];
                        area.city = null;
                        area.districts = [];
                        area.district = null;
                        ajax.ajax(url, method, {
                            userId: userId,
                            parentCode: area.area.code,
                            level: area.area.level + 1
                        }).success(function (data) {
                            area.provinces = data.list;
                            if (initAreaCode) {
                                for (var i in area.provinces) {
                                    var p = area.provinces[i];
                                    if (initAreaCode >= p.code && initAreaCode - p.code < 10000) {
                                        area.province = p;
                                        area.getCities(isInit);
                                        if (initAreaCode == p.code) {
                                            initAreaCode = null;
                                        }
                                    }
                                }
                            }
                        });
                    },
                    getCities: function (isInit) {
                        area.area = area.province == null ? defaultArea : area.province;
                        area.citys = [];
                        area.city = null;
                        area.districts = [];
                        area.district = null;
                        area.onSelect(area.area, isInit);
                        if (!area.province) {
                            return;
                        }
                        ajax.ajax(url, method, {
                            userId: userId,
                            parentCode: area.area.code,
                            level: area.area.level + 1
                        }).success(function (data) {
                            area.cities = data.list;
                            if (initAreaCode) {
                                for (var i in area.cities) {
                                    var p = area.cities[i];
                                    if (initAreaCode >= p.code && initAreaCode - p.code < 100) {
                                        area.city = p;
                                        area.getDistricts(isInit);
                                        if (initAreaCode == p.code) {
                                            initAreaCode = null;
                                        }
                                    }
                                }
                            }
                        });
                    },
                    getDistricts: function (isInit) {
                        area.area = area.city == null ? area.province : area.city;
                        area.districts = [];
                        area.district = null;
                        area.onSelect(area.area, isInit);
                        if (!area.city) {
                            return;
                        }
                        ajax.ajax(url, method, {
                            userId: userId,
                            parentCode: area.area.code,
                            level: area.area.level + 1
                        }).success(function (data) {
                            area.districts = data.list;
                            if (initAreaCode) {
                                for (var i in area.districts) {
                                    var p = area.districts[i];
                                    if (initAreaCode >= p.code && initAreaCode - p.code < 1) {
                                        area.district = p;
                                        area.selectDistrict(isInit);
                                        if (initAreaCode == p.code) {
                                            initAreaCode = null;
                                        }
                                    }
                                }
                            }
                        });
                    },
                    selectDistrict: function (isInit) {
                        area.area = area.district == null ? area.city : area.district;
                        area.onSelect(area.area, isInit);
                    }
                };
                area.getProvinces(true);
                return area;
            }
        })
        .directive("area", function (area) {
            return {
                restrict: 'A',
                replace: true,
                scope: {
                    change: "&",
                    service: "&"
                },
                template: '<span><label>省<select class="form-control m-l" ng-model="area.province" ng-change="area.getCities()"ng-options="a as a.name for a in area.provinces"><option value="">请选择</option></select></label>' +
                '&nbsp;<label class="m-l-md">市<select class="form-control m-l" ng-model="area.city" ng-change="area.getDistricts()"ng-options="a as a.name for a in area.cities"><option value="">请选择</option></select></label>' +
                '&nbsp;<label class="m-l-md">区<select class="form-control m-l" ng-model="area.district" ng-change="area.selectDistrict()"ng-options="a as a.name for a in area.districts"><option value="">请选择</option></select></label></span>',
                link: function (scope) {
                    scope.area = area.getArea(function (area, isInit) {
                        scope.change({$area: area, $isInit: isInit});
                        //console.info("change(area:" + area + ",  isInit:" + isInit + ")");
                    });
                    scope.service({$area: scope.area});
                }
            }
        });
    /**高德地图*/
    angular.module("mapModule", [])
        .directive("map", function () {
            return {
                restrict: 'A',
                scope: {
                    service: "&",
                    change: "&"
                },
                link: function (scope, element, attr) {
                    var defaultPoint = [116.404, 39.915];
                    var level = 6;
                    var map = new AMap.Map(attr['id'], {
                        zoom: level,
                        center: defaultPoint
                    });
                    AMap.plugin(['AMap.ToolBar', 'AMap.Scale'], function () {
                        var toolBar = new AMap.ToolBar();
                        var scale = new AMap.Scale();
                        map.addControl(toolBar);
                        map.addControl(scale);
                    });
                    var marker = new AMap.Marker({
                        position: defaultPoint,
                        animation: 'AMAP_ANIMATION_BOUNCE',
                        map: map
                    });
                    map.on('click', function (e) {
                        var point = [e.lnglat.lng, e.lnglat.lat];
                        marker.setPosition(point);
                        scope.change({
                            $lng: point[0],
                            $lat: point[1]
                        })
                    });
                    scope.service({
                        $map: {
                            change: function (lng, lat, level) {
                                var point = lng && lat ? [lng, lat] : defaultPoint;
                                var level = level == 0 ? 6 : (level == 1 ? 8 : (level == 2 ? 12 : 14));
                                marker.setPosition(point);
                                map.setZoomAndCenter(level, point);
                            }
                        }
                    });
                }
            }
        });
    /**警告框
     * 使用: alertService.show("操作成功!", "success", "80%");
     */
    angular.module("alertModule", [])
        .service("alertService", function () {
            this.show = function (msg, color, size) {
                var element = $("#" + "alertDiv");
                if (element.length <= 0) {//不存在就创建
                    var tempString = '<div id="alertDiv" class="text-center"' +
                        ' style="width: 100%;height: 40px;position: fixed;background-color: transparent;' +
                        ' top: 5px;left: 0;z-index: 3000;">' +
                        '<div class="alert alert-_color center-block text-center" ' +
                        ' style="width: _size;padding-top: 5px;padding-bottom: 5px;">' +
                        '<span style="font-size: 20px;"><i class="icon-ok"></i>&nbsp;_msg</span></div>' +
                        '</div>';
                    tempString = tempString.replace("_msg", msg)
                        .replace("_color", color)
                        .replace("_size", size);
                    var $alert = $(tempString);
                    $(document.body).append($alert);
                    element = $("#" + "alertDiv");
                    element.hide();//否则第一次没有淡入效果
                }
                element.fadeIn(500).delay(1000).slideUp("normal").hide("normal");
            };
        });

    /**将以上module汇总为baseModule,应尽量减少重复注入
     * filterModule
     * entityModule <- ngFileUpload
     * ajaxModule
     * pageModule
     * listModule <- pageModule ajaxModule
     * areaModule <- ajaxModule
     * mapModule
     */
    angular.module("baseModule", ["filterModule", "entityModule", "pageModule", "listModule",
        "areaModule", "mapModule", "alertModule"]);
})(angular, jQuery);
