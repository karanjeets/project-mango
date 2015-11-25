/**
 *  JS for index.html
 */

(function(){
	var app = angular.module("mango", ['nvd3']);
	
	app.controller("pieChartController", function($scope, $http) {
		
		$scope.options = {
	            chart: {
	                type: 'pieChart',
	                height: 500,
	                x: function(d){return d.key;},
	                y: function(d){return d.value;},
	                showLabels: true,
	                duration: 500,
	                labelThreshold: 0.01,
	                labelSunbeamLayout: true,
	                legend: {
	                    margin: {
	                        top: 5,
	                        right: 35,
	                        bottom: 5,
	                        left: 0
	                    }
	                }
	            }
	        };
		
		$http({
            method: "GET",
            url: '/mango/FetchChart?chart=cityVsSales'
        })
        .then(function(response) {
        	console.log(response.data)
        	$scope.data = response.data;
        });
		
	});
	
	
	
	app.controller("stackedAreaChartController", function($scope, $http){
		
		$scope.options = {
	            chart: {
	                type: 'stackedAreaChart',
	                height: 450,
	                margin : {
	                    top: 20,
	                    right: 20,
	                    bottom: 30,
	                    left: 40
	                },
	                x: function(d){return d[0];},
	                y: function(d){return d[1];},
	                useVoronoi: false,
	                clipEdge: true,
	                duration: 100,
	                useInteractiveGuideline: true,
	                xAxis: {
	                    showMaxMin: false,
	                    tickFormat: function(d) {
	                        return d3.time.format('%x')(new Date(d))
	                    }
	                },
	                yAxis: {
	                    tickFormat: function(d){
	                        return d3.format(',.2f')(d);
	                    }
	                },
	                zoom: {
	                    enabled: true,
	                    scaleExtent: [1, 10],
	                    useFixedDomain: false,
	                    useNiceScale: false,
	                    horizontalOff: false,
	                    verticalOff: true,
	                    unzoomEventType: 'dblclick.zoom'
	                }
	            }
	        };
		
		//$scope.data = [{"key":"Armalite","values":[[1355299200000,1]]},{"key":"Benelli","values":[[1419926400000,1],[1422000000000,1],[1344236400000,1],[1301814000000,1]]},{"key":"Browning","values":[[1398322800000,1],[1432364400000,1],[1431759600000,1],[1372230000000,1],[1351062000000,1]]}]
		
		
			$http({
	            method: "GET",
	            url: '/mango/FetchChart?chart=monthVsManufacturer'
	        })
	        .then(function(response) {
	        	console.log(response.data)
	        	$scope.data = response.data;
	        });
		
	});
	
})();
