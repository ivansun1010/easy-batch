/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package io.github.benas.easybatch.tutorials.jms;

import io.github.benas.easybatch.core.api.EasyBatchReport;
import io.github.benas.easybatch.core.impl.EasyBatchEngine;
import io.github.benas.easybatch.core.impl.EasyBatchEngineBuilder;
import io.github.benas.easybatch.flatfile.dsv.DelimitedRecordMapper;
import io.github.benas.easybatch.tutorials.common.Greeting;
import io.github.benas.easybatch.tutorials.jmx.GreetingSlowProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
* Main class to run the JMS tutorial.
 *
* @author benas (md.benhassine@gmail.com)
*/
public class Launcher {

    public static void main(String[] args) throws Exception {

        //start embedded JMS broker
        JMSUtil.startBroker();

        // Build easy batch engines
        EasyBatchEngine easyBatchEngine1 = buildEasyBatchEngine(1);
        EasyBatchEngine easyBatchEngine2 = buildEasyBatchEngine(2);

        //create a 2 threads pool to call Easy Batch engines in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(easyBatchEngine1);
        executorService.submit(easyBatchEngine2);

        executorService.shutdown();

    }

    public static EasyBatchEngine buildEasyBatchEngine(int id) {
        return new EasyBatchEngineBuilder()
                .registerRecordReader(new GreetingJmsReader(id))
                .registerRecordMapper(new DelimitedRecordMapper<Greeting>(Greeting.class, new String[]{"id","name"}))
                .registerRecordProcessor(new GreetingSlowProcessor())
                .build();
    }

}