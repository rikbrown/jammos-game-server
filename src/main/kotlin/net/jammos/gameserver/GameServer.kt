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
import net.jammos.gameserver.characters.*
import net.jammos.gameserver.network.handler.*
import net.jammos.gameserver.network.message.coding.ClientMessageDecoder
import net.jammos.gameserver.network.message.coding.ServerMessageEncoder
import net.jammos.utils.auth.Username
import net.jammos.utils.auth.crypto.CryptoManager
import net.jammos.utils.auth.dao.RedisAuthDao
import java.net.InetAddress
import java.time.Instant

private const val PORT = 1234
private const val TIMEOUT = 100



class GameServer {
    companion object {
        private val redis = RedisClient.create("redis://localhost")
        private val cryptoManager = CryptoManager()
        private val authDao = RedisAuthDao(redis, cryptoManager)
        private val authValidator = SessionAuthValidator(authDao, cryptoManager)
        private val characterDao = RedisCharacterDao(redis)
        private val characterListManager = CharacterListManager(characterDao)

        init {
            val rikUser = authDao.getUserAuth(Username.username("rik")) ?: authDao.createUser(Username.username("rik"), "1234")
            val bannedUser = authDao.getUserAuth(Username.username("banned")) ?: authDao.createUser(Username.username("banned"), "banned")
            val suspendedUser = authDao.getUserAuth(Username.username("suspended")) ?: authDao.createUser(Username.username("suspended"), "suspended")

            authDao.suspendUser(
                    userId = bannedUser.userId,
                    start = Instant.now(),
                    end = null)
            authDao.suspendUser(
                    userId = suspendedUser.userId,
                    start = Instant.now(),
                    end = Instant.now())

            characterDao.createCharacter(GameCharacter(
                    userId = rikUser.userId,
                    id = CharacterId(1),
                    name = "Rikalorgh",
                    race = Race.Orc,
                    characterClass = CharacterClass.Warrior,
                    gender = Gender.Male,
                    skin = 0,
                    face = 0,
                    hairStyle = 0,
                    hairColour = 0,
                    facialHair = 0,
                    level = 60,
                    zone = 0,
                    map = 0,
                    x = 0F,
                    y = 0F,
                    z = 0F,
                    guildId = 0,
                    flags = GameCharacter.Flags(
                            ghost = true
                    ),
                    firstLogin = false,
                    petId = 0,
                    petLevel = 0,
                    petFamily = 0))
            characterDao.createCharacter(GameCharacter(
                    userId = rikUser.userId,
                    id = CharacterId(2),
                    name = "Mazornus",
                    race = Race.Orc,
                    characterClass = CharacterClass.Warrior,
                    gender = Gender.Male,
                    skin = 0,
                    face = 0,
                    hairStyle = 0,
                    hairColour = 0,
                    facialHair = 0,
                    level = 60,
                    zone = 0,
                    map = 0,
                    x = 0F,
                    y = 0F,
                    z = 0F,
                    guildId = 0,
                    flags = GameCharacter.Flags(),
                    firstLogin = false,
                    petId = 0,
                    petLevel = 0,
                    petFamily = 0))
        }

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
                                        CharacterListHandler(characterListManager),
                                        CharacterCreateHandler(characterListManager),
                                        CharacterDeleteHandler(characterListManager))
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