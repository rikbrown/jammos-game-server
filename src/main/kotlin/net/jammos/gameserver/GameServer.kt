package net.jammos.gameserver

import com.lambdaworks.redis.RedisClient
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.ReadTimeoutHandler
import net.jammos.gameserver.auth.SessionAuthValidator
import net.jammos.gameserver.characters.CharacterListManager
import net.jammos.gameserver.network.handler.AuthSessionHandler
import net.jammos.gameserver.network.handler.CharacterListHandler
import net.jammos.gameserver.network.handler.PingHandler
import net.jammos.gameserver.network.handler.RequireAuthenticationHandler
import net.jammos.gameserver.network.message.coding.ClientMessageDecoder
import net.jammos.gameserver.network.message.coding.ServerMessageEncoder
import net.jammos.utils.auth.crypto.CryptoManager
import net.jammos.utils.auth.dao.RedisAuthDao
import java.net.InetAddress

private const val PORT = 1234
private const val TIMEOUT = 100



class GameServer {
    companion object {
        private val redis = RedisClient.create("redis://localhost")
        private val cryptoManager = CryptoManager()
        private val authDao = RedisAuthDao(redis, cryptoManager)
        private val authValidator = SessionAuthValidator(authDao, cryptoManager)
        private val characterListManager = CharacterListManager()

        @JvmStatic fun main(args: Array<String>) {

            // Configure the server.
            val bossGroup = NioEventLoopGroup(1)
            val workerGroup = NioEventLoopGroup()
            try {
                val b = ServerBootstrap()

                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel::class.java)
                        .option(ChannelOption.SO_BACKLOG, 100)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .option(ChannelOption.SO_REUSEADDR, true)
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100)
                        .handler(LoggingHandler(LogLevel.INFO))
                        .childHandler(object : ChannelInitializer<SocketChannel>() {
                            @Throws(Exception::class)
                            public override fun initChannel(ch: SocketChannel) {
                                val p = ch.pipeline()
                                p.addLast(
                                        ClientMessageDecoder(),
                                        ServerMessageEncoder(),
                                        ReadTimeoutHandler(TIMEOUT),

                                        // ** Unauthenticated handlers **
                                        // Auth session
                                        AuthSessionHandler(authValidator),

                                        // ** From now on, require authentication **
                                        RequireAuthenticationHandler(),

                                        // ** Authenticated handlers **
                                        // Always respond to pings
                                        PingHandler,
                                        CharacterListHandler(characterListManager))
                            }
                        })

                // Start the server.
                val hostAddress = InetAddress.getLoopbackAddress()
                val f = b.bind(hostAddress, PORT).sync()

                // Wait until the server socket is closed.
                f.channel().closeFuture().sync()
            } finally {
                // Shut down all event loops to terminate all threads.
                bossGroup.shutdownGracefully()
                workerGroup.shutdownGracefully()
            }
        }
    }
}