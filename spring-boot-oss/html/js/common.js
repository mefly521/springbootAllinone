/**
 * 公用的js函数文件
 */

var setting = {
  //"serverHost": "http://200.200.3.39:8083"
  //"serverHost": "http://200.200.3.38:8084"
   //"serverHost": "http://localhost:8088"
  "serverHost": "http://127.0.0.1:8081"
}
String.prototype.format = function (args) {
  var result = this;
  if (arguments.length > 0) {
    if (arguments.length == 1 && typeof (args) == "object") {
      for (var key in args) {
        if (args[key] != undefined) {
          var reg = new RegExp("({" + key + "})", "g");
          result = result.replace(reg, args[key]);
        }
      }
    } else {
      for (var i = 0; i < arguments.length; i++) {
        if (arguments[i] != undefined) {
          var reg = new RegExp("({[" + i + "]})", "g");
          result = result.replace(reg, arguments[i]);
        }
      }
    }
  }
  return result;
}


function ajax(param) {
  /**
  $.ajax({
  	url: param.url,
  	method: 'POST',
  	data: param.data,
  	contentType: false, // 注意这里应设为false
  	processData: false,
  	cache: false,
  	success: function(data) {
  		console.log(data);
  	},
  	error: function(jqXHR) {
  		console.log(JSON.stringify(jqXHR));
  	}
  })
  	**/
  $.ajax({
    type: param.type ? param.type : "POST",
    url: setting.serverHost + param.url,
    dataType: "json",
    contentType: param.contentType ? param.contentType : "application/json",
    // xhrFields: {
    //   withCredentials: true
    // },
    beforeSend: function (request) {
      console.info("===",param);
      if (param.headers && param.headers.length > 0) {
        $(param.headers).each(function (i, item) {
          console.info(item);
          
		  request.setRequestHeader(item.key, item.value);
        });
      }
	  request.setRequestHeader("at", "647dcb36b88040c490960280d07bc4b4");
	  request.setRequestHeader("tid", "1");
      if (getToken()) {
        request.setRequestHeader("token", getToken());
      }
    },
    async: !param.async ? param.async : true,
    traditional: !param.traditional ? param.traditional : true,
    data: param.data,
    success: function (data) {
      if (data.code == 0) {
        param.success(data);
      } else {
        if (data.code == 401) {
          layer.alert("会话结束,请重新登录", function (index) {
            location.href = "login.html";
          });
        } else {
          alert(data.msg);
        }
      }
    },
    error: param.error
  });
  return false;
}

function ajaxForChromeExtension(param) {
  $.ajax({
    type: param.type ? param.type : "POST",
    url: param.url,
    dataType: "json",
    contentType: param.contentType ? param.contentType : "application/json",
    xhrFields: {
      withCredentials: true
    },
    beforeSend: function (request) {
      if (param.headers && param.headers.length > 0) {
        $(param.headers).each(function (i, item) {
          console.info(item);
          request.setRequestHeader(item.key, item.value);
        });
      }
    },
    async: !param.async ? param.async : true,
    traditional: !param.traditional ? param.traditional : true,
    data: param.data,
    success: function (data, textStatus, jqXHR) {
      param.success(data, textStatus, jqXHR);
    },
    error: param.error
  });
  return false;
}

function checkLogin() {
  var userInfo = getCookie("userInfo");
  if (!userInfo) {
    $("#userInfo").html("");
    $("#loginButton").html("<a href='login.html'>登录</a>");
  } else {
    userInfo = $.parseJSON(userInfo);
    console.info(userInfo);
    $("#userName").text(userInfo.username);
  }
}

function getToken() {
  var userInfo = getCookie("userInfo");
  if (userInfo) {
    userInfo = $.parseJSON(userInfo);
    return userInfo.token;
  }
}
/**
 * 伪造http referer信息
 * 用 document.all 来判断当前的浏览器是否是IE， 如果是的话就生成一个link，
 * 然后自动执行 onclick 事件，如果不是的话就用JS 跳转。这样在处理页面就可以得到 HTTP_REFERER
 * @param url
 */
function referURL(url) {
  var isIe = (document.all) ? true : false;
  //console.info("isIe:"+isIe);
  if (isIe) {
    var linka = document.createElement('a');
    linka.href = url;
    document.body.appendChild(linka);
    linka.click();
  }
}



/**
 * 判断是否登录，没登录刷新当前页，促使Shiro拦截后跳转登录页
 * @param result	ajax请求返回的值
 * @returns {如果没登录、登录失效，刷新当前页}
 */
function isLogin(result) {
  if (result && result.code && (result.code == '1101' || result.code == '1102')) {
    window.location.reload(true); //刷新当前页
  }
  return true; //返回true
}

function logout() {

  layui.use('layer', function () {
    var layer = layui.layer;
    layer.confirm('是否退出登录?', function (index) {
      setCookie("token", null);
      location.href = "login.html";
    });
  })
}
/**
 * 针对不同的错误可结合业务自定义处理方式
 * @param result
 * @returns {Boolean}
 */
function isError(result) {
  var flag = true;
  if (result && result.status) {
    flag = false;
    if (result.status == '-1' || result.status == '-101' || result.status == '400' || result.status == '404' || result
      .status ==
      '500') {
      layer.alert(result.data);
    } else if (result.status == '403') {
      layer.alert(result.data, function () {
        //跳转到未授权界面
        window.location.href = "/403";
      });
    }
  }
  return flag; //返回true
}

/**
 * 获取get请求参数
 * @param name
 * @returns
 */
function getQueryString(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
  var search = window.location.search;
  if (search != null && search != "") {
    var r = search.substr(1).match(reg);
    if (r != null) {
      return unescape(r[2]);
    }
  }
  return null;
}

function _checkCourse(source) {
  course = getCookie("course");
  if (!course) {
    $.message({
      message: '没有选择课程，请选择课程',
      type: 'error'
    });
    location.href = "courseSelect.html?source="+source;
  }
}
/**
 * 获取菜单uri
 * @returns
 */
function getCallback() {
  var pathname = window.location.pathname;
  var param = GetQueryString("callback");
  //console.log("pathname:"+pathname);
  //console.log("param:"+param);
  if (param != null && param != "") {
    return param;
  } else {
    return pathname;
  }
}

//删除数组自定义函数
Array.prototype.remove = function (dx) {
  if (isNaN(dx) || dx > this.length) {
    return false;
  }
  for (var i = 0, n = 0; i < this.length; i++) {
    if (this[i] != this[dx]) {
      this[n++] = this[i]
    }
  }
  this.length -= 1
}

/**
 * 共同ajax方法
 * @returns
 */

function ajaxError(qXHR, textStatus, errorThrown) {
  console.info("qXHR", qXHR);
  console.info("textStatus", textStatus);
  console.info("errorThrown", errorThrown);

}

//获取url中的参数
function getRequest() {
  var url = location.search; //获取url中"?"符后的字串   
  var theRequest = new Object();
  if (url.indexOf("?") != -1) {
    var str = url.substr(1);
    strs = str.split("&");
    for (var i = 0; i < strs.length; i++) {
      theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
    }
  }
  return theRequest;
}

//没权限跳转
function noAuthProcess() {
  layui.use('layer', function (index) {
    var layer = layui.layer;
    layer.open({
      content: '会话已过期,请重新登录',
      yes: function (index, layero) {
        layer.close(index); //如果设定了yes回调，需进行手工关闭
        top.window.location.href = "login.html";
      }
    });
  });
}
// ajax 后通用回调方法
function commonProcessData(data) {
  if (data.code == 401) {
    noAuthProcess();
  }
  if (data.code != 1) {
    layui.use('layer', function (index) {
      var layer = layui.layer;
      layer.msg("系统内部错误");
    });
  }
}

function getFormData($form) {
  var unindexed_array = $form.serializeArray();
  var indexed_array = {};
  $.map(unindexed_array, function (n, i) {
    indexed_array[n['name']] = n['value'];
  });
  return indexed_array;
}

function setCookie(c_name, value, expiredays) {
  var exdate = new Date()
  exdate.setDate(exdate.getDate() + expiredays)
  document.cookie = c_name + "=" + escape(value) +
    ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
}

function delCookie(name) {
  var exp = new Date();
  exp.setTime(exp.getTime() - 1);
  var cval = getCookie(name);
  if (cval != null)
    document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

function getCookie(c_name) {
  if (document.cookie.length > 0) {
    c_start = document.cookie.indexOf(c_name + "=")
    if (c_start != -1) {
      c_start = c_start + c_name.length + 1
      c_end = document.cookie.indexOf(";", c_start)
      if (c_end == -1) c_end = document.cookie.length
      return unescape(document.cookie.substring(c_start, c_end))
    }
  }
  return ""
}
$(function () {

})