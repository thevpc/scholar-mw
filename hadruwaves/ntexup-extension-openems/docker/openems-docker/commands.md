docker login -u thevpc

docker build -t thevpc/openems:0.0.36 .
docker push thevpc/openems:0.0.36

mkdir openems-docker && cd openems-docker


docker run --rm   -v $(pwd)/simulations:/simulations   thevpc/openems:0.0.36   openEMS /simulations/test.xml --numThreads=2
