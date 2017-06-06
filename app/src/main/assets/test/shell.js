(function() {
	var globalCallbackId = 0;
	var nullMethoCallbackId = 'cb_null';
	var systemInfo = '';

	var localRecords = []; // 本地存储键值对在页面的缓存，在页面载入的时候，将所有本地键值对读取到这里，用户进行增删改查操作时，同步对此字典进行。

	var pageLoadReady = function() {};

	// 固定的方法
	function setupWebViewJavascriptBridge(callback) {
		if(window.WebViewJavascriptBridge) {
			return callback(WebViewJavascriptBridge);
		}
		if(window.WVJBCallbacks) {
			return window.WVJBCallbacks.push(callback);
		}
		window.WVJBCallbacks = [callback];
		var WVJBIframe = document.createElement('iframe');
		WVJBIframe.style.display = 'none';
		WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
		document.documentElement.appendChild(WVJBIframe);
		setTimeout(function() {
			document.documentElement.removeChild(WVJBIframe)
		}, 0);
	}
	// 运行环境初始化
	function environmentInitialize() {

		setupWebViewJavascriptBridge(function(bridge) {
			bridge.callHandler("initializeMethod", {
				"param": "value"
			}, function responseCallback(responseData) {});
			bridge.registerHandler(nullMethoCallbackId, function(data) {
				// 什么都不做
			});
			bridge.callHandler('getLocalStorages', {}, function responseCallback(responseData) {
				setTimeout(function() {
					var doc = document;
					var readyEvent = doc.createEvent('Events');
					readyEvent.initEvent('plusready');
					doc.dispatchEvent(readyEvent);
				}, 0);
				if(responseData) {
					var response = responseData;
					if(response.success == 'true') {
						localRecords = response.content;
					}
				} else {}
			});
		});
	}

	// 以回调ID注册回调，返回回调ID
	function registerCallback(func) {
		if(func == null) {
			return 'cb_null';
		}
		var callbackId = 'cb_' + globalCallbackId;
		globalCallbackId = globalCallbackId + 1;
		setupWebViewJavascriptBridge(function(bridge) {
			bridge.registerHandler(callbackId, function(data, callback) {
				func(data);
			});
		});
		return callbackId;
	}

	function funcWithReturn(funcName, params) {
		var toReturn = null;
		setupWebViewJavascriptBridge(function(bridge) {
			bridge.callHandler(funcName, params, function responseCallback(responseData) {
				toReturn = responseData;
			});
		});
	}
	var androidshell = {
		saveKVData: function(params) {
			if(!window.WebViewJavascriptBridge) {
				console.log("WebViewJavascriptBridge未初始化");
			}
			params = typeof params == "string" ? params : JSON.stringify(params);
			var data = {
				"funName": "saveKVData",
				"params": params
			};
			var ret = prompt("jsBridge://" + JSON.stringify(data));
			return ret;
		},
		getKVData: function(params) {
			if(!window.WebViewJavascriptBridge) {
				console.log("WebViewJavascriptBridge未初始化");
			}
			params = typeof params == "string" ? params : JSON.stringify(params);
			var data = {
				"funName": "getKVData",
				"params": params
			};
			var ret = prompt("jsBridge://" + JSON.stringify(data));
			return ret;
		},
		delKVData: function(params) {
			if(!window.WebViewJavascriptBridge) {
				console.log("WebViewJavascriptBridge未初始化");
			}
			params = typeof params == "string" ? params : JSON.stringify(params);
			var data = {
				"funName": "delKVData",
				"params": params
			};
			var ret = prompt("jsBridge://" + JSON.stringify(data));
			return ret;
		},
		getNetworkState: function(params) {
			if(!window.WebViewJavascriptBridge) {
				console.log("WebViewJavascriptBridge未初始化");
			}
			params = typeof params == "string" ? params : JSON.stringify(params);
			var data = {
				"funName": "getNetworkState",
				"params": params
			};
			var ret = prompt("jsBridge://" + JSON.stringify(data));
			return ret;
		},
		selectFiles: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'selectFiles', params, callback
			);
		},
		checkUpdate: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'checkUpdate', params, callback
			);
		},
		doMedia: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'doMedia', params, callback
			);
		},
		joinMeeting: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'joinMeeting', params, callback
			);
		},
		uploadFiles: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'uploadFiles', params, callback
			);
		},
		getMediaBase64Infos: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'getMediaBase64Infos', params, callback
			);
		},
		browseMedia: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'browseMedia', params, callback
			);
		},
		checkUpdate: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'checkUpdate', params, callback
			);
		},
		getLocations: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'getLocations', params, callback
			);
		},
		getThumbnails: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'getThumbnails', params, callback
			);
		},
		setUserProfile: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'setUserProfile', params, callback
			);
		},
		takeMedia: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'takeMedia', params, callback
			);
		},
		pickMediaFiles: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'pickMediaFiles', params, callback
			);
		},
		playMedia: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'playMedia', params, callback
			);
		},
		clearCache: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'clearCache', params, callback
			);
		},
		checkjsVersion: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'checkjsVersion', params, callback
			);
		},
		videoPreviewEdit: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'videoPreviewEdit', params, callback
			);
		},
		quit: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'quit', params, callback
			);
		},
		dialNumberUrl: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'dialNumberUrl', params, callback
			);
		},
		takeMediaUpload: function(params, callback) {
			var jsonParams = JSON.parse(params);
			var action = jsonParams['actionName'];
			var taskId = jsonParams['taskId'];
			var tenantId = jsonParams['tenantId'];
			var storageURL = jsonParams['storageURL'];
			var fileStatusNotifyURL = jsonParams['fileStatusNotifyURL'];
			var fileSessionId = jsonParams['fileSessionId'];
			var isRename = jsonParams['isRename'];
			var uploadTrunkInfoURL = jsonParams['uploadTrunkInfoURL'];
			var isHttp = (-1 != storageURL.indexOf('http://')) || (-1 != storageURL.indexOf('https://'));
			if(tenantId == undefined || tenantId == null || tenantId == '') {
				tenantId = '';
			}
			var callbackFn = function(argument) {
				argument = typeof argument == "string" ? JSON.parse(argument) : argument;
				var success = argument['success'];
				var description = argument['description'];
				var fileInfos = argument['fileInfos'];
				var fileInfo = fileInfos[0];
				var fileName = fileInfo['name'];
				var fileType = fileInfo['fileType'];
				var fileLocalPath = fileInfo['localPath'];
				var fileSize = fileInfo['fileSize'];
				var callbackUploadFiles = function(arguments) {
					arguments = typeof arguments == "string" ? JSON.parse(arguments) : arguments;
					var args = [{
						'success': arguments['success'],
						'description': arguments['description'],
						'fileInfo': {
							'fileName': fileName,
							'fileType': fileType,
							'fileSize': fileSize,
							'filePath': isHttp ? '' : taskId,
							'tenantId': tenantId
						}
					}];
					if(typeof callback === "function") {
						callback.apply(null, args);
					}
				}
				var inputJson = {
					'taskId': taskId,
					'storageURL': storageURL,
					'tenantId': tenantId,
					'fileStatusNotifyURL': fileStatusNotifyURL,
					'filesLocalPathArr': [{
						'path': fileLocalPath,
						'indexNO': 0,
						'fileSessionId': fileSessionId,
						'isRename': isRename
					}]
				};
				var inputJsonStr = JSON.stringify(inputJson);

				androidshell.uploadFiles(inputJsonStr, callbackUploadFiles);
			};
			if(action == 'takePhoto' || action == 'recordVideo' || action == 'recordAudio') {

				var inputJson = {
					'actionName': action
				};
				var inputJsonStr = JSON.stringify(inputJson);

				androidshell.takeMedia(inputJsonStr, callbackFn);
			} else if(action == 'pickMediaFile') {

				var inputJson = {
					'allowSelectNum': 1
				};
				var inputJsonStr = JSON.stringify(inputJson);
				androidshell.pickMediaFiles(inputJsonStr, callbackFn);
			}
		},
		sendMessageUrl: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'sendMessageUrl', params, callback
			);
		},
		startUpdateLocation: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'startUpdateLocation', params, callback
			);
		},
		stopUpdateLocation: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'stopUpdateLocation', params, callback
			);
		},
		querySkip: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'querySkip', params, callback
			);
		},
		setWebview: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'setWebview', params, callback
			);
		},
		exit: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'exit', params, callback
			);
		},
		updatePortal: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'updatePortal', params, callback
			);
		},
		getLoginPath: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'getLoginPath', params, callback
			);
		},
		listMeeting: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'listMeeting', params, callback
			);
		},

		bindGeTuiCid: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'bindGeTuiCid', params, callback
			);
		},
		setScrollIndicator: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'setScrollIndicator', params, callback
			);
		},
		createWindow: function(params) {
			params = typeof params == "string" ? params : JSON.stringify(params);
			var data = {
				"funName": "createWindow",
				"params": params
			};
			var ret = prompt("jsBridge://" + JSON.stringify(data));
			return ret;
		},
		showWindow: function(params) {
			params = typeof params == "string" ? params : JSON.stringify(params);
			var data = {
				"funName": "showWindow",
				"params": params
			};
			var ret = prompt("jsBridge://" + JSON.stringify(data));
			return ret;
		},
		closeWindow: function(params) {
			params = typeof params == "string" ? params : JSON.stringify(params);
			var data = {
				"funName": "closeWindow",
				"params": params
			};
			var ret = prompt("jsBridge://" + JSON.stringify(data));
			return ret;
		},
		evalJS: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'evalJS', params, callback
			);
		},
		getAllWebview: function(params) {
			params = typeof params == "string" ? params : JSON.stringify(params);
			var data = {
				"funName": "getAllWebview",
				"params": params
			};
			var ret = prompt("jsBridge://" + JSON.stringify(data));
			if(ret) {
				ret = JSON.parse(ret);
				ret = ret.ids;
			}
			return ret;
		},
		getWebviewById: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'getWebviewById', params, callback
			);
		},
		getWebviewId: function(params) {
			params = typeof params == "string" ? params : JSON.stringify(params);
			var data = {
				"funName": "getWebviewId",
				"params": params
			};
			var ret = prompt("jsBridge://" + JSON.stringify(data));
			if(ret) {
				ret = JSON.parse(ret);
				ret = ret.id;
			}
			return ret;
		},
		showWaiting: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'showWaiting', params, callback
			);
		},
		removeWaiting: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'removeWaiting', params, callback
			);
		},
		webviewGoTop: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'webviewGoTop', params, callback
			);
		},
		setWebviewInvisible: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'setWebviewInvisible', params, callback
			);
		},
		setWebviewVisible: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'setWebviewVisible', params, callback
			);
		},
		overWriteAndroidBackKey: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'overWriteAndroidBackKey', params, callback
			);
		},
		clearWebviews: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'clearWebviews', params, callback
			);
		},
		alert: function(params, callback) {
			params = typeof params == "string" ? params : JSON.stringify(params);
			var data = {
				"funName": "alert",
				"params": params
			};
			var ret = prompt("jsBridge://" + JSON.stringify(data));
			return ret;
		},
		goHome: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'goHome', params, callback
			);
		},
		toast: function(params, callback) {
			window.WebViewJavascriptBridge.callHandler(
				'toast', params, callback
			);
		}
	};
	var Utils = {
		getMobileOS: function() {
			var u = navigator.userAgent;
			var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
			var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
			if(isAndroid) {
				return "Android";
			}
			if(isiOS) {
				return "iOS";
			}
			return "PC";
		}
	}

	var iosshell = {
		createWindow: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('createWindow', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		showWindow: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('showWindow', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		evalJS: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('evalJS', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		closeWindow: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('closeWindow', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		jumpToWebView: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('jumpToWebView', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		takeMedia: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('takeMedia', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {
					alert('done');
				});
			});
		},
		pickMediaFiles: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('pickMediaFiles', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {
					alert('done');
				});
			});
		},
		selectFiles: function(params, callback) {
			var callbackFn = function(argument) {
				var success = argument['success'];
				var description = argument['description'];
				var fileInfos = argument['fileInfos'];
				var i;

				var fileLocalPathArrNew = new Array();
				for(i = 0; i < fileInfos.length; i++) {
					var fileInfo = fileInfos[i];
					var fileLocalPath = fileInfo['localPath'];
					fileLocalPathArrNew.push({
						'path': fileLocalPath,
						'indexNO': i
					});
				}

				var inputJson = {
					'filesLocalPathArr': fileLocalPathArrNew
				};
				var inputJsonStr = JSON.stringify(inputJson);
				iosshell.getThumbnails(inputJsonStr, callback);
			};

			iosshell.pickMediaFiles(params, callbackFn);
		},
		uploadFiles: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('uploadFiles', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		saveKVData: function(params) {
			var cb = null;
			var cbid = registerCallback(cb);
			var obj;
			if(typeof params == 'string') {
				obj = JSON.parse(params);
			} else {
				obj = params;
			}
			var key = obj.key;
			var value = obj.value;
			localRecords[key] = value;
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('setKVData', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		getKVData: function(params) {
			var obj;
			if(typeof params == 'string') {
				obj = JSON.parse(params);
			} else {
				obj = params;
			}
			var key = obj.key;
			return localRecords[key];
		},
		delKVData: function(params) {
			var callback = null;
			var cb = typeof callback != 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			var obj;
			if(typeof params == 'string') {
				obj = JSON.parse(params);
			} else {
				obj = params;
			}
			delete localRecords[obj.key];
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('delKVData', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		getMediaBase64Infos: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('getMediaBase64Infos', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		getThumbnails: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('getThumbnails', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		clearCache: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('clearCache', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		browseMedia: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('browseMedia', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		getLocations: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('getLocations', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		startUpdateLocation: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('startUpdateLocation', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		getNetworkState: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('getNetworkState', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		stopUpdateLocation: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('stopUpdateLocation', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		setUserProfile: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('setUserProfile', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		takeMediaUpload: function(params, callback) {
			var jsonParams = JSON.parse(params);
			var action = jsonParams['actionName'];
			var taskId = jsonParams['taskId'];
			var tenantId = jsonParams['tenantId'];
			var storageURL = jsonParams['storageURL'];
			var fileStatusNotifyURL = jsonParams['fileStatusNotifyURL'];
			var fileSessionId = jsonParams['fileSessionId'];
			var isRename = jsonParams['isRename'];
			var uploadTrunkInfoURL = jsonParams['uploadTrunkInfoURL'];
			var isHttp = (-1 != storageURL.indexOf('http://')) || (-1 != storageURL.indexOf('https://'));
			if(tenantId == undefined || tenantId == null || tenantId == '') {
				tenantId = '';
			}
			var callbackFn = function(argument) {
				argument = typeof argument == "string" ? JSON.parse(argument) : argument;
				var success = argument['success'];
				var description = argument['description'];
				var fileInfos = argument['fileInfos'];
				var fileInfo = fileInfos[0];
				var fileName = fileInfo['name'];
				var fileType = fileInfo['fileType'];
				var fileLocalPath = fileInfo['localPath'];
				var fileSize = fileInfo['fileSize'];
				var callbackUploadFiles = function(arguments) {
					arguments = typeof arguments == "string" ? JSON.parse(arguments) : arguments;
					var args = [{
						'success': arguments['success'],
						'description': arguments['description'],
						'fileInfo': {
							'fileName': fileName,
							'fileType': fileType,
							'fileSize': fileSize,
							'filePath': isHttp ? '' : taskId,
							'tenantId': tenantId
						}
					}];
					if(typeof callback === "function") {
						callback.apply(null, args);
					}
				}
				var inputJson = {
					'taskId': taskId,
					'storageURL': storageURL,
					'tenantId': tenantId,
					'fileStatusNotifyURL': fileStatusNotifyURL,
					'filesLocalPathArr': [{
						'path': fileLocalPath,
						'indexNO': 0,
						'fileSessionId': fileSessionId,
						'isRename': isRename
					}]
				};
				var inputJsonStr = JSON.stringify(inputJson);
				iosshell.uploadFiles(inputJsonStr, callbackUploadFiles);
			};

			if(action == 'takePhoto' || action == 'recordVideo' || action == 'recordAudio') {
				var inputJson = {
					'actionName': action
				};
				var inputJsonStr = JSON.stringify(inputJson);
				iosshell.takeMedia(inputJsonStr, callbackFn);
			} else if(action == 'pickMediaFile') {
				var inputJson = {
					'allowSelectNum': 1
				};
				var inputJsonStr = JSON.stringify(inputJson);
				iosshell.pickMediaFiles(inputJsonStr, callbackFn);
			}
		},
		playMedia: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('playMedia', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		goHome: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('goHome', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		doMedia: function(params, callback) {
			var callbackFn = function(argument) {
				var success = argument['success'];
				var description = argument['description'];
				var fileInfos = argument['fileInfos'];
				var i;

				var fileLocalPathArrNew = new Array();
				for(i = 0; i < fileInfos.length; i++) {
					var fileInfo = fileInfos[i];
					var fileLocalPath = fileInfo['localPath'];

					fileLocalPathArrNew.push({
						'path': fileLocalPath,
						'indexNO': i
					});
				}

				var inputJson = {
					'filesLocalPathArr': fileLocalPathArrNew
				};
				var inputJsonStr = JSON.stringify(inputJson);
				iosshell.getThumbnails(inputJsonStr, callback);
			};
			iosshell.takeMedia(params, callbackFn);
		},
		checkjsVersion: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('checkjsVersion', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		videoPreviewEdit: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('videoPreviewEdit', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		dialNumberUrl: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('dialNumberUrl', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		sendMessageUrl: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('sendMessageUrl', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		bindGeTuiCid: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('bindGeTuiCid', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		querySkip: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('querySkip', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		clearWebviews: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('clearWebviews', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		setWebView: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('setWebView', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		getAllWebView: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('getAllWebView', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		getWebViewId: function(params, callback) {
			var value = iosshell.getKVData({
				"key": "pageId"
			});
			return value;
		},
		showWaiting: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('showWaiting', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		},
		removeWaiting: function(params, callback) {
			var cb = typeof callback !== 'function' ? null : function(args) {
				callback(args);
			};
			var cbid = registerCallback(cb);
			setupWebViewJavascriptBridge(function(bridge) {
				bridge.callHandler('removeWaiting', {
					'callbackId': cbid,
					'param': params
				}, function responseCallback(responseData) {;
				});
			});
		}
	};
	var shell = {
		storage: {
			setItem: function(key, value) {
				if(key && value && typeof key == "string" && typeof value == "string") {
					if(Utils.getMobileOS() == "Android") {
						androidshell.saveKVData({
							"key": key,
							"value": value
						});
					} else if(Utils.getMobileOS() == "iOS") {
						iosshell.saveKVData({
							"key": key,
							"value": value
						});
					} else {
						console.log("unkonwOS");
					}
					return true;
				} else {
					return false;
				}
			},
			getItem: function(key) {
				if(key && typeof key == "string") {
					var value = "";
					if(Utils.getMobileOS() == "Android") {
						value = androidshell.getKVData({
							"key": key
						});
						value = JSON.parse(value);
						if(value) {
							value = value.content;
						} else {
							value = null;
						}
					} else if(Utils.getMobileOS() == "iOS") {
						value = iosshell.getKVData({
							"key": key
						});
					} else {
						console.log("unkonwOS");
					}
					return value;
				} else {
					return undefined;
				}
			},
			removeItem: function(key) {
				if(key && typeof key == "string") {
					if(Utils.getMobileOS() == "Android") {
						androidshell.delKVData({
							"key": key
						});
					} else if(Utils.getMobileOS() == "iOS") {
						iosshell.delKVData({
							"key": key
						});
					} else {
						console.log("unkonwOS");
					}
					return true;
				} else {
					return false;
				}
			}
		},

		saveKVData: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				var ret = androidshell.saveKVData(params);
				if(callback) {
					callback(ret);
				}
				return ret;
			} else if(Utils.getMobileOS() == "iOS") {
				var ret = iosshell.saveKVData(params, callback);
				if(callback) {
					callback(ret);
				}
				return ret;
			} else {
				console.log("unkonwOS");
			}
		},
		getKVData: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				var ret = androidshell.getKVData(params);
				ret = typeof ret == "string" ? JSON.parse(ret) : ret;
				if(callback) {
					callback(ret);
				}
				return ret;
			} else if(Utils.getMobileOS() == "iOS") {
				var ret = iosshell.getKVData(params, callback);
				ret = {
					"success": "true",
					"content": ret,
					"description": ""
				}
				if(callback) {
					callback(ret);
				}
				return ret;
			} else {
				console.log("unkonwOS");
			}
		},
		delKVData: function(params,callback) {
			if(Utils.getMobileOS() == "Android") {
				var ret = androidshell.delKVData(params);
				if(callback) {
					callback(ret);
				}
				return ret;
			} else if(Utils.getMobileOS() == "iOS") {
				var ret = iosshell.delKVData(params, callback);
				if(callback) {
					callback(ret);
				}
				return ret;
			} else {
				console.log("unkonwOS");
			}
		},
		selectFiles: function(params, callback) {
			var cb = function(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				if(callback) {
					callback(data);
				}
			}
			if(Utils.getMobileOS() == "Android") {
				androidshell.selectFiles(params, cb);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.selectFiles(params, cb);
			} else {
				console.log("unkonwOS");
			}
		},
		checkUpdate: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.checkUpdate(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.checkUpdate(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		doMedia: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.doMedia(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.doMedia(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		joinMeeting: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.joinMeeting(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.joinMeeting(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		uploadFiles: function(params, callback) {
			var cb = function(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				if(callback) {
					callback(data);
				}
			}
			if(Utils.getMobileOS() == "Android") {
				androidshell.uploadFiles(params, cb);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.uploadFiles(params, cb);
			} else {
				console.log("unkonwOS");
			}
		},
		getMediaBase64Infos: function(params, callback) {
			var cb = function(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				if(callback) {
					callback(data);
				}
			}
			if(Utils.getMobileOS() == "Android") {
				androidshell.getMediaBase64Infos(params, cb);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.getMediaBase64Infos(params, cb);
			} else {
				console.log("unkonwOS");
			}
		},
		browseMedia: function(params, callback) {
			var cb = function(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				if(callback) {
					callback(data);
				}
			}
			if(Utils.getMobileOS() == "Android") {
				androidshell.browseMedia(params, cb);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.browseMedia(params, cb);
			} else {
				console.log("unkonwOS");
			}
		},
		checkUpdate: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.checkUpdate(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.checkUpdate(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		getLocations: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.getLocations(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.getLocations(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		getThumbnails: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.getThumbnails(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.getThumbnails(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		setUserProfile: function(params, callback) {
			var cb = function(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				if(callback) {
					callback(data);
				}
			}
			if(Utils.getMobileOS() == "Android") {
				androidshell.setUserProfile(params, cb);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.setUserProfile(params, cb);
			} else {
				console.log("unkonwOS");
			}
		},
		takeMedia: function(params, callback) {
			var cb = function(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				if(callback) {
					callback(data);
				}
			}
			if(Utils.getMobileOS() == "Android") {
				androidshell.takeMedia(params, cb);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.takeMedia(params, cb);
			} else {
				console.log("unkonwOS");
			}
		},
		pickMediaFiles: function(params, callback) {
			var cb = function(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				if(callback) {
					callback(data);
				}
			}
			if(Utils.getMobileOS() == "Android") {
				androidshell.pickMediaFiles(params, cb);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.pickMediaFiles(params, cb);
			} else {
				console.log("unkonwOS");
			}
		},
		playMedia: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.playMedia(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.playMedia(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		clearCache: function(params, callback) {
			var cb = function(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				if(callback) {
					callback(data);
				}
			}
			if(Utils.getMobileOS() == "Android") {
				androidshell.clearCache(params, cb);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.clearCache(params, cb);
			} else {
				console.log("unkonwOS");
			}
		},
		checkjsVersion: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.checkjsVersion(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.checkjsVersion(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		videoPreviewEdit: function(params, callback) {
			var cb = function(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				if(callback) {
					callback(data);
				}
			}
			if(Utils.getMobileOS() == "Android") {
				androidshell.videoPreviewEdit(params, cb);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.videoPreviewEdit(params, cb);
			} else {
				console.log("unkonwOS");
			}
		},
		dialNumberUrl: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.dialNumberUrl(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.dialNumberUrl(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		sendMessageUrl: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.sendMessageUrl(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.sendMessageUrl(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		startUpdateLocation: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.startUpdateLocation(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.startUpdateLocation(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		stopUpdateLocation: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.stopUpdateLocation(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.stopUpdateLocation(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		querySkip: function(params, callback) {
			var cb = function(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				if(callback) {
					callback(data);
				}
			}
			if(Utils.getMobileOS() == "Android") {
				androidshell.querySkip(params, cb);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.querySkip(params, cb);
			} else {
				console.log("unkonwOS");
			}
		},
		setWebview: function(params, callback) {
			var cb = function(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				if(callback) {
					callback(data);
				}
			}
			if(Utils.getMobileOS() == "Android") {
				androidshell.setWebview(params, cb);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.setWebView(params, cb);
			} else {
				console.log("unkonwOS");
			}
		},
		exit: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.exit(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.exit(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		updatePortal: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.updatePortal(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				//TODO
			} else {
				console.log("unkonwOS");
			}
		},
		getLoginPath: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.getLoginPath(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				//TODO
			} else {
				console.log("unkonwOS");
			}
		},
		listMeeting: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.listMeeting(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.listMeeting(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},

		bindGeTuiCid: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.bindGeTuiCid(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.bindGeTuiCid(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		createWindow: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.createWindow(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.createWindow(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		openWindow: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				params = typeof params == "string" ? params : JSON.stringify(params);
				androidshell.createWindow(params);
				androidshell.showWindow(params);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.createWindow(params, function() {
					iosshell.showWindow(params, callback);
				});
			} else {
				console.log("unkonwOS");
			}
		},
		showWindow: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.showWindow(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.showWindow(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		closeWindow: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.closeWindow(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.closeWindow(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		close: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.closeWindow(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.closeWindow(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		evalJS: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.evalJS(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.evalJS(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		getAllWebview: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				return androidshell.getAllWebview(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.getAllWebView(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		getWebviewById: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				//				return androidshell.getAllWebview(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				//iosshell.evalJS(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		getWebviewId: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				return androidshell.getWebviewId(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				return iosshell.getWebViewId(params, callback); 
			} else {
				console.log("unkonwOS");
			}
		},
		showWaiting: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.showWaiting(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.showWaiting(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		takeMediaUpload: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.takeMediaUpload(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.takeMediaUpload(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		removeWaiting: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.removeWaiting(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.removeWaiting(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		webviewGoTop: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.webviewGoTop(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				//				iosshell.webviewGoTop(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		setWebviewInvisible: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.setWebviewInvisible(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				//				iosshell.webviewGoTop(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		setWebviewVisible: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.setWebviewVisible(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				//				iosshell.webviewGoTop(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		overWriteAndroidBackKey: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.overWriteAndroidBackKey(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				//				iosshell.webviewGoTop(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		jumpToWebView: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				//				androidshell.overWriteAndroidBackKey(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.jumpToWebView(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		clearWebviews: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.clearWebviews(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.clearWebviews(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		getNetworkState: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				var ret = androidshell.getNetworkState(params);
				ret = JSON.parse(ret);
				if(callback) {
					callback(ret.networkState);
				}
				return ret.networkState;
			} else if(Utils.getMobileOS() == "iOS") {
				//TODO				iosshell.getNetworkState(params, callback);
				return true;
			} else {
				console.log("unkonwOS");
			}
		},
		alert: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.alert(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				var text;
				if(params) {
					params = typeof params == "string" ? params : JSON.stringify(params);
					text = JSON.parse(params);
					if(text) {
						text = text.text;
					} else {
						text = params;
					}
				}
				alert(text);
			} else {
				console.log("unkonwOS");
			}
		},
		setScrollIndicator: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.setScrollIndicator(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.setScrollIndicator(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		toast: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.toast(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				var text;
				if(params) {
					params = typeof params == "string" ? params : JSON.stringify(params);
					text = JSON.parse(params);
					if(text) {
						text = text.text;
					} else {
						text = params;
					}
				}
				alert(text);
			} else {
				console.log("unkonwOS");
			}
		},
		getWindow: function(data) {
			var id = shell.getWebviewId();
			if(data) {
				data = typeof data == "string" ? JSON.parse(data) : data;
				id = data.id;
			} 
			return {
				id: id,
				close: function() {
					shell.closeWindow({
						"ids": [id]
					});
				},
				evalJS: function(js) {
					shell.evalJS({
						"id": id,
						"js": js
					});
				}
			};
		},
		goHome: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.goHome(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				iosshell.goHome(params, callback);
			} else {
				console.log("unkonwOS");
			}
		},
		quit: function(params, callback) {
			if(Utils.getMobileOS() == "Android") {
				androidshell.quit(params, callback);
			} else if(Utils.getMobileOS() == "iOS") {
				//				iosshell.quit(params, callback);
			} else {
				console.log("unkonwOS");
			}
		}
	}
	var plus = {
		nativeUI: {
			toast: function(text) {
				shell.toast({
					"text": text
				});
			}
		},
		key: {
			addEventListener: function(params, callback) {
				if(params == "backbutton" && callback) {
					plus.blackFun = callback;
					shell.overWriteAndroidBackKey({
						"js": "plus.blackFun()"
					});
				}
			}
		},
		webview: {
			all: function() {
				var webArray = [];
				var all = shell.getAllWebview();
				for(var i = 0; i < all.length; i++) {
					webArray.push({
						"id": all[i],
						"close": function() {
							shell.closeWindow({
								"ids": [all[i]]
							})
						}
					});
				}
				return webArray;
			},
			close: function(id) {
				shell.closeWindow({
					"ids": [id]
				})
			},
			currentWebview: function() {
				return {
					"id": shell.getWebviewId(),
					"close": function() {
						shell.closeWindow({
							"ids": [all[i]]
						})
					},
					"setStyle": function(params) {
						var scrollIndicator = "";
						if(params) {
							scrollIndicator = params.scrollIndicator;
						}
						if(scrollIndicator) {
							shell.setScrollIndicator({
								"scrollIndicator": scrollIndicator
							});
						}
					}
				};
			},
			create: function(id, url) {
				shell.createWindow({
					"id": id,
					"url": url
				})
			},
			show: function() {
				shell.showWindow({
					"id": id
				})
			},
			getWebviewById: function(id) {
				return {
					close: function() {
						shell.closeWindow({
							"ids": [id]
						});
					},
					evalJS: function(js) {
						shell.evalJS({
							"js": js,
							"id": id
						});
					},
					id: id,
				}
			}
		},
		storage: {
			setItem: function(key, value) {
				if(key && value && typeof key == "string" && typeof value == "string") {
					if(Utils.getMobileOS() == "Android") {
						androidshell.saveKVData({
							"key": key,
							"value": value
						});
					} else if(Utils.getMobileOS() == "iOS") {
						iosshell.saveKVData({
							"key": key,
							"value": value
						});
					} else {
						console.log("unkonwOS");
					}
					return true;
				} else {
					return false;
				}
			},
			getItem: function(key) {
				if(key && typeof key == "string") {
					var value = "";
					if(Utils.getMobileOS() == "Android") {
						value = androidshell.getKVData({
							"key": key
						});
						value = JSON.parse(value);
						if(value) {
							value = value.content;
						} else {
							value = null;
						}
					} else if(Utils.getMobileOS() == "iOS") {
						value = iosshell.getKVData({
							"key": key
						});
					} else {
						console.log("unkonwOS");
					}
					return value;
				} else {
					return undefined;
				}
			},
			removeItem: function(key) {
				if(key && typeof key == "string") {
					if(Utils.getMobileOS() == "Android") {
						androidshell.delKVData({
							"key": key
						});
					} else if(Utils.getMobileOS() == "iOS") {
						iosshell.delKVData({
							"key": key
						});
					} else {
						console.log("unkonwOS");
					}
					return true;
				} else {
					return false;
				}
			}
		}

	}
	window.shell = shell;
	window.plus = plus;
	window.Utils = Utils;
	if(Utils.getMobileOS() == "iOS") {
		environmentInitialize();
	} else if(Utils.getMobileOS() == "Android") {
		document.addEventListener(
			'WebViewJavascriptBridgeReady',
			function() {
				WebViewJavascriptBridge.init();
				setTimeout(function() {
					var doc = document;
					var readyEvent = doc.createEvent('Events');
					readyEvent.initEvent('plusready');
					doc.dispatchEvent(readyEvent);
				}, 0);
			},
			false
		);
	}
})();