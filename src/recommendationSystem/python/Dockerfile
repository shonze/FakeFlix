FROM python:3

WORKDIR /usr/src/app

COPY src/recommendationSystem/python/requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

ENTRYPOINT ["python", "src/recommendationSystem/python/Client.py"]