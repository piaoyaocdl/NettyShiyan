package com.hsw.NettyShiyan;

import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerAdapter;

/**
 * 处理事件
 */
public class DiscardServerHandler extends ChannelHandlerAdapter
{

	// 读取管道信息
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
		((ByteBuf) msg).release();
	}

	// 异常发生时调用
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		cause.printStackTrace();
		ctx.close();
	}
}