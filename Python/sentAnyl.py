import tweepy
from textblob import TextBlob

consumer_key = #filler
consumer_secret = #filler

access_token = #filler
access_token_secret = #filler

auth = tweepy.OAuthHandler(consumer_key,consumer_secret)
auth.set_access_token(access_token,access_token_secret)

api = tweepy.API(auth)

public_tweets = api.search("Trump")

for tweet in public_tweets:
    print(tweet.text)
    analysis = TextBlob(tweet.text)
    print(analysis.sentiment)
