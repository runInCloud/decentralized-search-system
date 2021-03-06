package com.fly.house.fileshare;

import com.fly.house.fileshare.handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by dimon on 3/26/14.
 */
@Component
public class ProtocolInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private FileShareHandler handler;

    @Autowired
    private HttpValidatorHandler httpValidatorHandler;

    @Autowired
    private CacheHandler cacheHandler;

    @Autowired
    private FileAccessHandler fileAccessHandler;

    @Autowired
    private PathDecoder pathDecoder;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("decompressor", new HttpContentDecompressor());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
        pipeline.addLast("httpValidatorHandler", httpValidatorHandler);
        pipeline.addLast("cacheHandler", cacheHandler);
        pipeline.addLast("pathDecoder", pathDecoder);
        pipeline.addLast("fileAccessHandler", fileAccessHandler);
        pipeline.addLast("handler", handler);
        pipeline.addLast("encoder", new HttpResponseEncoder());
    }
}
