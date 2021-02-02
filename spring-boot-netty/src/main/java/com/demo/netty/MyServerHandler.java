package com.demo.netty;

import java.util.UUID;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<String> {
	/**
	 * 每次传入的消息都要调用
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
		System.out.println(channelHandlerContext.channel().remoteAddress()+","+s);
		channelHandlerContext.channel().writeAndFlush("from service"+UUID.randomUUID());
		Thread.sleep(1000L);
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("来自服务端的问候");
	}
	/**
	 * 异常捕获
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}