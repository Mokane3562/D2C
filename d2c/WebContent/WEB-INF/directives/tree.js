app.directive('tree', ['$compile', function($compile){
	return {
		restrict: "E",
		replace: true,
		scope: {
			tree: '='
		},
		template: '<div><node ng-repeat="node in tree" node="node" ng-click="node.onClick($event)" ng-dblclick="node.onDblClick()" ></node></div>',
		link: function(scope, element, attriutes){
			$compile(element.contents())(scope);
		}
	};
}]);