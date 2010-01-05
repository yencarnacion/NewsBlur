from django.conf.urls.defaults import *
from apps.reader import views

urlpatterns = patterns('',
    (r'^$', views.index),
    (r'^load_single_feed', views.load_single_feed),
    (r'^load_feed_page', views.load_feed_page),
    (r'^load_feeds', views.load_feeds),
    (r'^refresh_feed', views.refresh_feed),
    (r'^mark_story_as_read', views.mark_story_as_read),
    (r'^mark_story_as_like', views.mark_story_as_like),
    (r'^mark_story_as_dislike', views.mark_story_as_dislike),
    (r'^mark_feed_as_read', views.mark_feed_as_read),
    (r'^get_read_feed_items', views.get_read_feed_items),
    (r'^get_read_feed_items', views.get_read_feed_items),
)
