package com.hsw.NettyShiyan;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Discards any incoming data.
 */
public class DiscardServer
{

	private int port;

	public DiscardServer(int port)
	{
		this.port = port;
	}

	public void run() throws Exception
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // 用来接收进来的连接
		EventLoopGroup workerGroup = new NioEventLoopGroup();// 用来处理已经被接收的连接
		try
		{
			ServerBootstrap b = new ServerBootstrap(); // 启动NIO服务的辅助启动类
			b.group(bossGroup, workerGroup).//
					channel(NioServerSocketChannel.class) // 指定管道的类型
					.childHandler(new ChannelInitializer<SocketChannel>()
					{ // (4)
						@Override
						public void initChannel(SocketChannel ch) throws Exception
						{
							ch.pipeline().addLast(new DiscardServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port).sync(); // (7)

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to
			// gracefully
			// shut down your server.
			f.channel().closeFuture().sync();
		} finally
		{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception
	{
		int port;
		if (args.length > 0)
		{
			port = Integer.parseInt(args[0]);
		} else
		{
			port = 8080;
		}
		new DiscardServer(port).run();
	}
}