import tweepy
from textblob import TextBlob

consumer_key = 'vw7FNaJVb9fclrSjQDpgbMRfu'
consumer_secret = 'yYbDzP4u4LbUNQxwXenlpp95HPq4ietHxXRamtovqev9xochFT'

access_token = "962193561121824769-KTGVVSCyZwz1ikrL5vjZOANDHKRCawA"
access_token_secret = "q6oFScgrC6aNbFbPpb1ltl7kiPxxNEN8Ta58IFrGMRZoy"

auth = tweepy.OAuthHandler(consumer_key,consumer_secret)
auth.set_access_token(access_token,access_token_secret)

api = tweepy.API(auth)

public_tweets = api.search("Trump")

for tweet in public_tweets:
    print(tweet.text)
    analysis = TextBlob(tweet.text)
    print(analysis.sentiment)
