package com.android.toolkitlibrary.network.http;


import com.android.toolkitlibrary.network.utils.FFLogUtil;

public abstract class FFNetWorkCallBack<T>{

	private static final String TAG = "FFNetWorkCallBack";
	
	/**
	 * 当activity finished 将不再执行，除非指定
	 * @param response
	 */
	public void success(T response){
		if(onSuccess(response)){
			return;
		}
		
	}
	
	public abstract boolean onSuccess(T response);

	/**
	 * 当activity finished 将不再执行，除非指定
	 *
	 */
	public String fail(FFNetWorkRequest<T> request, T response) {
		FFLogUtil.e(TAG, "网络请求发生错误"+request.getErrMessage());
		if(onFail(response)){
			return null;
		}
		String msg = null;
		switch (request.getStatus()) {
		case ERROR_NATIVE_NET_CLOST://网络未连接
			msg = "网络未连接！";
			break;
		case ERROR_NET_TIMEOUT_R://请求超时
			msg = "您的网络状况不佳，操作失败！";
			break;
		case ERROR_NET_TIMEOUT_S://连接超时
			msg = "您的网络状况不佳，操作失败！";
			break;
		case UNSET://未处理
			msg = "请求失败";
			break;
		case ERROR_ANALYSIS://数据解析
			msg = "服务器返回的数据解析失败";
			break;
		case ERROR_NET_404://404
			msg = "网络异常";
			break;
		case ERROR_SITE_505://505
			msg = "服务器出错";
			break;
		case ERROR_IO://IO异常
			msg = "您的网络状况不佳，操作失败！";
			break;
		case ERROR_DATA:
			msg = "服务器返回的数据解析失败";
			break;
		case ERROR_SITE_XXX:
			msg = "网络错误 ";
			break;
		case SUCCESS:
			break;
		case ERROR_BY_SERVICE:
			msg = request.getErrMessage();
			break;
		default:
			break;
		}
		if(msg == null){
			return request.getErrMessage();
		}
		return msg;
	}

	public abstract boolean onFail(T response);

}
