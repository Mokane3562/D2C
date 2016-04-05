app.directive('node', ['$compile',function($compile){
	return{
		restrict: "E",
		replace: true,
		scope: {
			node: '='
		},
		template: '<div class="nodeDiv"><span class="words"> + </span></div>',
		link: function(scope, element, attributes){
			element.append(scope.node.name);
			if(angular.isArray(scope.node.contents)){
				element.append("<tree tree='node.contents' ng-show='node.visable'></tree>");
			}
			$compile(element.contents())(scope);
		}
	};
}]);