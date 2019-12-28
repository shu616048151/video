/**
 * Created by YTY on 2016/4/1.
 */
var dateRangeModule = angular.module('dateRangeModule', []);
dateRangeModule.directive('dateRange', function () {
    return {
        restrict: 'A',
        replace: true,
        scope: {
            start: "=",
            end: "=",
            change: "&"
        },
        template: '<span style="position:relative">' +
        '<input  type="text" class="rangeInput form-control" style="min-width: 320px">' +
        '<i class="icon-calendar text-primary" ng-click="dateRangeClick()" style="position:absolute;bottom:0;right:24px;top:auto;cursor:pointer;"></i>' +
        '</span>',
        link: function (scope, element, attrs) {
            scope.dateRangeClick = function () {
                $('.rangeInput').click();
            };
            var startInit = !scope.start ? moment().subtract(7, 'days') : moment(scope.start);
            var endInit = !scope.end ? moment() : moment(scope.end);
            scope.change({
                $startDate: moment(startInit).format("YYYY-MM-DD"),
                $endDate: moment(endInit).add(1, 'days').format("YYYY-MM-DD")
            });
            $('.rangeInput').daterangepicker({
                "ranges": {
                    "近一周": [
                        moment().subtract(7, 'days'),
                        moment()
                    ],
                    "近一月": [
                        moment().subtract(30, 'days'),
                        moment()
                    ],
                    "本月": [
                        moment().startOf('month'),
                        moment()
                    ],
                    "上月": [
                        moment().startOf('month').subtract(1, 'days').startOf('month'),
                        moment().startOf('month').subtract(1, 'days')
                    ]
                },
                "locale": {
                    "format": "YYYY年MM月DD日",
                    "separator": " 至 ",
                    "applyLabel": "确认",
                    "cancelLabel": "关闭",
                    "fromLabel": "从",
                    "toLabel": "至",
                    "customRangeLabel": "自定义",
                    "daysOfWeek": [
                        "日", "一", "二", "三", "四", "五", "六"
                    ],
                    "monthNames": [
                        "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"
                    ],
                    "firstDay": 1
                },
                "autoApply": true,
                "autoUpdateInput": true,
                "startDate": startInit,
                "endDate": endInit
            }, function (start, end) {
                scope.change({
                    $startDate: moment(start).format("YYYY-MM-DD"),
                    $endDate: moment(end).add(1, 'days').format("YYYY-MM-DD")
                });
            });
        }
    };
});
dateRangeModule.directive('singleDate',function(){
    return {
        restrict: 'A',
        replace: true,
        scope: {
            date: "=",
            change: "&"
        },
        template: '<span style="position:relative">' +
        '<input type="text" class="dateInput form-control" style="min-width: 320px">' +
        '<i class="icon-calendar text-primary" ng-click="dateClick()" style="position:absolute;bottom:0;right:24px;top:auto;cursor:pointer;"></i>' +
        '</span>',
        link:function (scope, element, attrs){
            scope.dateClick=function(){
                console.log("点击了input");
                $('.dateInput').click();
            };
            scope.dateFormat="DD日";
            $('.dateInput').daterangepicker({
                singleDatePicker: true,
                "locale":{
                    "format": scope.dateFormat,
                    "separator": "---"
                }
                //showDropdowns: true
            },function (start, end){
                scope.change({
                    $date: moment(start).format("YYYY-MM-DD")
                });
            })
        }
    }
});
dateRangeModule.directive('doubleDate',function(){
    return {
        restrict: 'A',
        replace: true,
        scope: {
            date: "=",
            change: "&"
        },
        template: '<span style="position:relative">' +
        '<input type="text" class="dateInput form-control" style="min-width: 320px;display: inline-block">' +
        '<i class="icon-calendar text-primary" ng-click="dateClick()" style="position:absolute;top:3px;right:15px;cursor:pointer;display: inline-block"></i>' +
        '</span>',
        link:function (scope, element, attrs){
            var startInit =!scope.start ? moment() : moment(scope.start);
            var endInit =!scope.end ? moment().add(1,'years') : moment(scope.end) ;
            scope.dateClick=function(){
                console.log("点击了input");
                $('.dateInput').click();
            };
            scope.dateFormat="YYYY年MM月DD日";
            $('.dateInput').daterangepicker({
                "locale":{
                    "format": scope.dateFormat,
                    "separator": "---",
                    "applyLabel": "确认",
                    "cancelLabel": "关闭",
                    "daysOfWeek": [
                        "日", "一", "二", "三", "四", "五", "六"
                    ],
                    "monthNames": [
                        "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"
                    ]
                },
                "startDate":startInit,
                "endDate":endInit
                //showDropdowns: true
            },function (start, end){
                scope.change({
                    $startDate: moment(start).format("YYYY.MM.DD"),
                    $endDate: moment(end).add(1, 'days').format("YYYY.MM.DD")
                });
            })
        }
    }
});