-- ============================================================
-- Consolidated sample data for curvy_dolphin
-- Original data + additional users/videos + cross-user interactions
-- + additional likes + additional comments
-- ============================================================

-- ============================================================
-- USERS
-- ============================================================
INSERT INTO curvy_dolphin.users
  (user_id, username, provider_id, email, name, avatar_url, created_at)
OVERRIDING SYSTEM VALUE
VALUES
  (1, 'jmiller', 'google-oauth2|1000000001', 'j.miller@example.com', 'Jordan Miller', 'https://cdn.curvydolphin.com/avatars/jmiller.png', '2026-06-01 08:15:00'::timestamp),
  (2, 'sarah_k', 'google-oauth2|1000000002', 'sarah.k@example.com', 'Sarah Kim', 'https://cdn.curvydolphin.com/avatars/sarahk.png', '2026-06-03 12:40:00'::timestamp),
  (3, 'devon.t', 'google-oauth2|1000000003', 'devon.t@example.com', 'Devon Turner', 'https://cdn.curvydolphin.com/avatars/devont.png', '2026-06-05 17:05:00'::timestamp),
  (4, 'alex.r', 'google-oauth2|1000000004', 'alex.r@example.com', 'Alex Rivera', 'https://cdn.curvydolphin.com/avatars/alexr.png', '2026-06-17 09:30:00'::timestamp),
  (5, 'mia.p', 'google-oauth2|1000000005', 'mia.p@example.com', 'Mia Parker', 'https://cdn.curvydolphin.com/avatars/miap.png', '2026-06-18 11:20:00'::timestamp),
  (6, 'chris.d', 'google-oauth2|1000000006', 'chris.d@example.com', 'Chris Davis', 'https://cdn.curvydolphin.com/avatars/chrisd.png', '2026-06-19 14:10:00'::timestamp);

SELECT setval(
  pg_get_serial_sequence('curvy_dolphin.users', 'user_id'),
  (SELECT MAX(user_id) FROM curvy_dolphin.users)
);

-- ============================================================
-- VIDEOS
-- ============================================================
INSERT INTO curvy_dolphin.videos
  (id, user_id, video_url, thumbnail_url, latitude, longitude, location_id, caption, created_at, visibility)
VALUES
  ('fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 1, 'https://cdn.curvydolphin.com/videos/v1001.mp4', 'https://cdn.curvydolphin.com/thumbs/v1001.jpg', 39.9526, -75.1652, 501, 'Sunset over the Schuylkill 🌅', '2026-06-10 18:30:00'::timestamptz, 'PUBLIC'),
  ('50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 1, 'https://cdn.curvydolphin.com/videos/v1002.mp4', 'https://cdn.curvydolphin.com/thumbs/v1002.jpg', 39.95, -75.161, 502, 'Trying this new coffee spot ☕', '2026-06-12 09:05:00'::timestamptz, 'PUBLIC'),
  ('196d1360-558c-4e28-b076-5dd78f62d81b'::uuid, 1, 'https://cdn.curvydolphin.com/videos/v1003.mp4', 'https://cdn.curvydolphin.com/thumbs/v1003.jpg', 39.954, -75.16, 503, 'Weekend hike recap', '2026-06-15 14:20:00'::timestamptz, 'FRIENDS'),
  ('b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 2, 'https://cdn.curvydolphin.com/videos/v2001.mp4', 'https://cdn.curvydolphin.com/thumbs/v2001.jpg', 40.7128, -74.006, 601, 'NYC skyline at night', '2026-06-11 20:00:00'::timestamptz, 'PUBLIC'),
  ('ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 2, 'https://cdn.curvydolphin.com/videos/v2002.mp4', 'https://cdn.curvydolphin.com/thumbs/v2002.jpg', 40.7306, -73.9866, 602, 'Cooking pasta from scratch 🍝', '2026-06-14 19:15:00'::timestamptz, 'PUBLIC'),
  ('89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 3, 'https://cdn.curvydolphin.com/videos/v3001.mp4', 'https://cdn.curvydolphin.com/thumbs/v3001.jpg', 34.0522, -118.2437, 701, 'Beach day 🏖️', '2026-06-13 15:45:00'::timestamptz, 'PUBLIC'),
  ('3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 3, 'https://cdn.curvydolphin.com/videos/v3002.mp4', 'https://cdn.curvydolphin.com/thumbs/v3002.jpg', 34.0195, -118.4912, 702, 'Skate park tricks', '2026-06-16 11:30:00'::timestamptz, 'PRIVATE'),

  -- Additional videos for users 4-6
  ('a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 4, 'https://cdn.curvydolphin.com/videos/v4001.mp4', 'https://cdn.curvydolphin.com/thumbs/v4001.jpg', 39.9528, -75.1641, 801, 'Best pizza I have had in Philly 🍕', '2026-06-18 18:20:00'::timestamptz, 'PUBLIC'),
  ('7d3b9a12-6e4c-4a91-9c8f-2f7d5b1a3001'::uuid, 4, 'https://cdn.curvydolphin.com/videos/v4002.mp4', 'https://cdn.curvydolphin.com/thumbs/v4002.jpg', 39.9534, -75.1618, 802, 'Walking through Old City', '2026-06-19 08:45:00'::timestamptz, 'PUBLIC'),
  ('c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 5, 'https://cdn.curvydolphin.com/videos/v5001.mp4', 'https://cdn.curvydolphin.com/thumbs/v5001.jpg', 40.4406, -79.9959, 901, 'A weekend in Pittsburgh', '2026-06-19 15:10:00'::timestamptz, 'PUBLIC'),
  ('e14f6a49-25e7-4b38-94f2-8a1c4d6e6004'::uuid, 5, 'https://cdn.curvydolphin.com/videos/v5002.mp4', 'https://cdn.curvydolphin.com/thumbs/v5002.jpg', 40.4421, -79.9972, 902, 'Sunset by the river', '2026-06-20 20:05:00'::timestamptz, 'PUBLIC'),
  ('f25a7b50-36f8-4c49-a5e3-9b2d5f7a7005'::uuid, 6, 'https://cdn.curvydolphin.com/videos/v6001.mp4', 'https://cdn.curvydolphin.com/thumbs/v6001.jpg', 39.7392, -104.9903, 1001, 'Denver mountain views', '2026-06-20 16:40:00'::timestamptz, 'PUBLIC'),
  ('b36c8d61-47a9-4d5a-b6f4-1c3e6a8b8006'::uuid, 6, 'https://cdn.curvydolphin.com/videos/v6002.mp4', 'https://cdn.curvydolphin.com/thumbs/v6002.jpg', 39.7395, -104.9891, 1002, 'Downtown after dark', '2026-06-21 21:30:00'::timestamptz, 'PUBLIC');

-- ============================================================
-- ORIGINAL VIDEO INTERACTIONS
-- ============================================================
INSERT INTO curvy_dolphin.video_interactions
  (user_id, video_id, event_type, watch_duration, video_duration, created_at, latitude, longitude)
VALUES
  (1, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-19 03:09:00'::timestamptz, 39.948393, -75.183269),
  (1, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'PLAY', NULL, 60, '2026-06-19 03:09:03'::timestamptz, 39.948393, -75.183269),
  (1, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_25', 15, 60, '2026-06-19 03:09:18'::timestamptz, 39.948393, -75.183269),
  (1, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_50', 30, 60, '2026-06-19 03:09:48'::timestamptz, 39.948393, -75.183269),
  (1, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_75', 45, 60, '2026-06-19 03:10:33'::timestamptz, 39.948393, -75.183269),
  (1, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_COMPLETE', 60, 60, '2026-06-19 03:11:33'::timestamptz, 39.948393, -75.183269),
  (1, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'LIKE', NULL, 60, '2026-06-19 03:11:36'::timestamptz, 39.948393, -75.183269),
  (1, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'SHARE', NULL, 60, '2026-06-19 03:11:43'::timestamptz, 39.948393, -75.183269),
  (1, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-17 17:58:00'::timestamptz, 39.952897, -75.1837),
  (1, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'PLAY', NULL, 50, '2026-06-17 17:58:02'::timestamptz, 39.952897, -75.1837),
  (1, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'WATCH_25', 12, 50, '2026-06-17 17:58:14'::timestamptz, 39.952897, -75.1837),
  (1, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'WATCH_50', 25, 50, '2026-06-17 17:58:39'::timestamptz, 39.952897, -75.1837),
  (1, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'WATCH_75', 37, 50, '2026-06-17 17:59:16'::timestamptz, 39.952897, -75.1837),
  (1, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'WATCH_COMPLETE', 50, 50, '2026-06-17 18:00:06'::timestamptz, 39.952897, -75.1837),
  (1, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'LIKE', NULL, 50, '2026-06-17 18:00:14'::timestamptz, 39.952897, -75.1837),
  (1, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'COMMENT', NULL, 50, '2026-06-17 18:00:17'::timestamptz, 39.952897, -75.1837),
  (2, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-18 16:05:00'::timestamptz, 40.714842, -74.023636),
  (2, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'PLAY', NULL, 45, '2026-06-18 16:05:03'::timestamptz, 40.714842, -74.023636),
  (2, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'WATCH_25', 11, 45, '2026-06-18 16:05:14'::timestamptz, 40.714842, -74.023636),
  (2, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'SKIP', 13, 45, '2026-06-18 16:05:17'::timestamptz, 40.714842, -74.023636),
  (2, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-17 17:36:00'::timestamptz, 40.716222, -74.024016),
  (2, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'PLAY', NULL, 35, '2026-06-17 17:36:01'::timestamptz, 40.716222, -74.024016),
  (2, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'WATCH_25', 8, 35, '2026-06-17 17:36:09'::timestamptz, 40.716222, -74.024016),
  (2, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'WATCH_50', 17, 35, '2026-06-17 17:36:26'::timestamptz, 40.716222, -74.024016),
  (2, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'SKIP', 19, 35, '2026-06-17 17:36:28'::timestamptz, 40.716222, -74.024016),
  (3, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-18 23:26:00'::timestamptz, 34.03797, -118.258988),
  (3, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'PLAY', NULL, 30, '2026-06-18 23:26:02'::timestamptz, 34.03797, -118.258988),
  (3, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'WATCH_25', 7, 30, '2026-06-18 23:26:09'::timestamptz, 34.03797, -118.258988),
  (3, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'WATCH_50', 15, 30, '2026-06-18 23:26:24'::timestamptz, 34.03797, -118.258988),
  (3, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'WATCH_75', 22, 30, '2026-06-18 23:26:46'::timestamptz, 34.03797, -118.258988),
  (3, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'WATCH_COMPLETE', 30, 30, '2026-06-18 23:27:16'::timestamptz, 34.03797, -118.258988),
  (3, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-20 09:52:00'::timestamptz, 34.05948, -118.259578),
  (1, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-18 10:23:00'::timestamptz, 39.936497, -75.156716),
  (1, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'PLAY', NULL, 45, '2026-06-18 10:23:03'::timestamptz, 39.936497, -75.156716),
  (1, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'WATCH_25', 11, 45, '2026-06-18 10:23:14'::timestamptz, 39.936497, -75.156716),
  (1, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'WATCH_50', 22, 45, '2026-06-18 10:23:36'::timestamptz, 39.936497, -75.156716),
  (1, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'WATCH_75', 33, 45, '2026-06-18 10:24:09'::timestamptz, 39.936497, -75.156716),
  (1, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'WATCH_COMPLETE', 45, 45, '2026-06-18 10:24:54'::timestamptz, 39.936497, -75.156716),
  (1, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'LIKE', NULL, 45, '2026-06-18 10:24:56'::timestamptz, 39.936497, -75.156716),
  (1, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'SHARE', NULL, 45, '2026-06-18 10:25:07'::timestamptz, 39.936497, -75.156716),
  (2, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-18 12:31:00'::timestamptz, 40.720016, -74.008896),
  (2, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'PLAY', NULL, 60, '2026-06-18 12:31:02'::timestamptz, 40.720016, -74.008896),
  (2, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_25', 15, 60, '2026-06-18 12:31:17'::timestamptz, 40.720016, -74.008896),
  (2, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_50', 30, 60, '2026-06-18 12:31:47'::timestamptz, 40.720016, -74.008896),
  (2, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_75', 45, 60, '2026-06-18 12:32:32'::timestamptz, 40.720016, -74.008896),
  (2, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_COMPLETE', 60, 60, '2026-06-18 12:33:32'::timestamptz, 40.720016, -74.008896),
  (2, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'LIKE', NULL, 60, '2026-06-18 12:33:41'::timestamptz, 40.720016, -74.008896),
  (2, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'COMMENT', NULL, 60, '2026-06-18 12:33:52'::timestamptz, 40.720016, -74.008896),
  (3, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-19 20:23:00'::timestamptz, 34.044191, -118.231925),
  (3, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'PLAY', NULL, 35, '2026-06-19 20:23:03'::timestamptz, 34.044191, -118.231925),
  (3, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'WATCH_25', 8, 35, '2026-06-19 20:23:11'::timestamptz, 34.044191, -118.231925),
  (3, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'SKIP', 10, 35, '2026-06-19 20:23:25'::timestamptz, 34.044191, -118.231925);

-- ============================================================
-- ADDITIONAL CROSS-USER VIDEO INTERACTIONS
-- ============================================================
INSERT INTO curvy_dolphin.video_interactions
  (user_id, video_id, event_type, watch_duration, video_duration, created_at, latitude, longitude)
VALUES
  -- Alex on old videos
  (4, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-19 18:42:00'::timestamptz, 39.949821, -75.166421),
  (4, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'PLAY', NULL, 60, '2026-06-19 18:42:02'::timestamptz, 39.949821, -75.166421),
  (4, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'WATCH_25', 15, 60, '2026-06-19 18:42:17'::timestamptz, 39.949821, -75.166421),
  (4, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'WATCH_50', 30, 60, '2026-06-19 18:42:32'::timestamptz, 39.949821, -75.166421),
  (4, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'WATCH_75', 45, 60, '2026-06-19 18:42:47'::timestamptz, 39.949821, -75.166421),
  (4, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'WATCH_COMPLETE', 60, 60, '2026-06-19 18:43:02'::timestamptz, 39.949821, -75.166421),
  (4, 'fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 'LIKE', NULL, 60, '2026-06-19 18:43:06'::timestamptz, 39.949821, -75.166421),
  (4, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-20 12:15:00'::timestamptz, 39.951102, -75.158843),
  (4, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'PLAY', NULL, 40, '2026-06-20 12:15:02'::timestamptz, 39.951102, -75.158843),
  (4, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'WATCH_25', 10, 40, '2026-06-20 12:15:12'::timestamptz, 39.951102, -75.158843),
  (4, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'SKIP', 13, 40, '2026-06-20 12:15:15'::timestamptz, 39.951102, -75.158843),
  (4, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-21 16:08:00'::timestamptz, 39.948322, -75.162110),
  (4, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'PLAY', NULL, 50, '2026-06-21 16:08:02'::timestamptz, 39.948322, -75.162110),
  (4, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'WATCH_25', 12, 50, '2026-06-21 16:08:14'::timestamptz, 39.948322, -75.162110),
  (4, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'WATCH_50', 25, 50, '2026-06-21 16:08:27'::timestamptz, 39.948322, -75.162110),
  (4, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'WATCH_75', 38, 50, '2026-06-21 16:08:40'::timestamptz, 39.948322, -75.162110),
  (4, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'WATCH_COMPLETE', 50, 50, '2026-06-21 16:08:52'::timestamptz, 39.948322, -75.162110),
  (4, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'COMMENT', NULL, 50, '2026-06-21 16:09:04'::timestamptz, 39.948322, -75.162110),

  -- Mia on old videos
  (5, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-19 09:12:00'::timestamptz, 40.713902, -74.007842),
  (5, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'PLAY', NULL, 30, '2026-06-19 09:12:02'::timestamptz, 40.713902, -74.007842),
  (5, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'WATCH_25', 7, 30, '2026-06-19 09:12:09'::timestamptz, 40.713902, -74.007842),
  (5, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'WATCH_50', 15, 30, '2026-06-19 09:12:17'::timestamptz, 40.713902, -74.007842),
  (5, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'WATCH_75', 22, 30, '2026-06-19 09:12:24'::timestamptz, 40.713902, -74.007842),
  (5, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'WATCH_COMPLETE', 30, 30, '2026-06-19 09:12:32'::timestamptz, 40.713902, -74.007842),
  (5, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'LIKE', NULL, 30, '2026-06-19 09:12:36'::timestamptz, 40.713902, -74.007842),
  (5, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-20 15:34:00'::timestamptz, 40.715221, -74.012331),
  (5, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'PLAY', NULL, 35, '2026-06-20 15:34:02'::timestamptz, 40.715221, -74.012331),
  (5, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'WATCH_25', 8, 35, '2026-06-20 15:34:10'::timestamptz, 40.715221, -74.012331),
  (5, '3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 'SKIP', 11, 35, '2026-06-20 15:34:13'::timestamptz, 40.715221, -74.012331),
  (5, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-21 21:05:00'::timestamptz, 40.718321, -74.004221),
  (5, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'PLAY', NULL, 60, '2026-06-21 21:05:03'::timestamptz, 40.718321, -74.004221),
  (5, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_25', 15, 60, '2026-06-21 21:05:18'::timestamptz, 40.718321, -74.004221),
  (5, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_50', 30, 60, '2026-06-21 21:05:33'::timestamptz, 40.718321, -74.004221),
  (5, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_75', 45, 60, '2026-06-21 21:05:48'::timestamptz, 40.718321, -74.004221),
  (5, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'WATCH_COMPLETE', 60, 60, '2026-06-21 21:06:03'::timestamptz, 40.718321, -74.004221),
  (5, 'b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 'SHARE', NULL, 60, '2026-06-21 21:06:09'::timestamptz, 40.718321, -74.004221),

  -- Chris on old videos
  (6, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-20 08:22:00'::timestamptz, 34.047812, -118.251221),
  (6, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'PLAY', NULL, 30, '2026-06-20 08:22:02'::timestamptz, 34.047812, -118.251221),
  (6, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'WATCH_25', 7, 30, '2026-06-20 08:22:09'::timestamptz, 34.047812, -118.251221),
  (6, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'WATCH_50', 15, 30, '2026-06-20 08:22:17'::timestamptz, 34.047812, -118.251221),
  (6, '50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 'SKIP', 17, 30, '2026-06-20 08:22:19'::timestamptz, 34.047812, -118.251221),
  (6, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-21 15:47:00'::timestamptz, 34.041221, -118.258812),
  (6, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'PLAY', NULL, 50, '2026-06-21 15:47:02'::timestamptz, 34.041221, -118.258812),
  (6, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'WATCH_25', 12, 50, '2026-06-21 15:47:14'::timestamptz, 34.041221, -118.258812),
  (6, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'WATCH_50', 25, 50, '2026-06-21 15:47:27'::timestamptz, 34.041221, -118.258812),
  (6, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'WATCH_COMPLETE', 50, 50, '2026-06-21 15:47:52'::timestamptz, 34.041221, -118.258812),
  (6, '89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 'LIKE', NULL, 50, '2026-06-21 15:47:56'::timestamptz, 34.041221, -118.258812),
  (6, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-22 19:42:00'::timestamptz, 34.045122, -118.249991),
  (6, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'PLAY', NULL, 40, '2026-06-22 19:42:02'::timestamptz, 34.045122, -118.249991),
  (6, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'WATCH_25', 10, 40, '2026-06-22 19:42:12'::timestamptz, 34.045122, -118.249991),
  (6, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'WATCH_50', 20, 40, '2026-06-22 19:42:22'::timestamptz, 34.045122, -118.249991),
  (6, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'WATCH_75', 30, 40, '2026-06-22 19:42:32'::timestamptz, 34.045122, -118.249991),
  (6, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'WATCH_COMPLETE', 40, 40, '2026-06-22 19:42:42'::timestamptz, 34.045122, -118.249991),
  (6, 'ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 'COMMENT', NULL, 40, '2026-06-22 19:42:51'::timestamptz, 34.045122, -118.249991),

  -- Jordan on new videos
  (1, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-19 20:11:00'::timestamptz, 39.952841, -75.164121),
  (1, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'PLAY', NULL, 42, '2026-06-19 20:11:02'::timestamptz, 39.952841, -75.164121),
  (1, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'WATCH_25', 10, 42, '2026-06-19 20:11:12'::timestamptz, 39.952841, -75.164121),
  (1, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'WATCH_50', 21, 42, '2026-06-19 20:11:23'::timestamptz, 39.952841, -75.164121),
  (1, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'WATCH_75', 31, 42, '2026-06-19 20:11:33'::timestamptz, 39.952841, -75.164121),
  (1, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'WATCH_COMPLETE', 42, 42, '2026-06-19 20:11:44'::timestamptz, 39.952841, -75.164121),
  (1, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'LIKE', NULL, 42, '2026-06-19 20:11:48'::timestamptz, 39.952841, -75.164121),
  (1, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-20 16:27:00'::timestamptz, 39.949221, -75.171003),
  (1, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'PLAY', NULL, 55, '2026-06-20 16:27:03'::timestamptz, 39.949221, -75.171003),
  (1, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'WATCH_25', 14, 55, '2026-06-20 16:27:17'::timestamptz, 39.949221, -75.171003),
  (1, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'WATCH_50', 28, 55, '2026-06-20 16:27:31'::timestamptz, 39.949221, -75.171003),
  (1, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'WATCH_COMPLETE', 55, 55, '2026-06-20 16:28:26'::timestamptz, 39.949221, -75.171003),
  (1, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'SHARE', NULL, 55, '2026-06-20 16:28:34'::timestamptz, 39.949221, -75.171003),
  (1, 'b36c8d61-47a9-4d5a-b6f4-1c3e6a8b8006'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-22 22:41:00'::timestamptz, 39.953412, -75.161221),
  (1, 'b36c8d61-47a9-4d5a-b6f4-1c3e6a8b8006'::uuid, 'PLAY', NULL, 25, '2026-06-22 22:41:02'::timestamptz, 39.953412, -75.161221),
  (1, 'b36c8d61-47a9-4d5a-b6f4-1c3e6a8b8006'::uuid, 'WATCH_25', 6, 25, '2026-06-22 22:41:08'::timestamptz, 39.953412, -75.161221),
  (1, 'b36c8d61-47a9-4d5a-b6f4-1c3e6a8b8006'::uuid, 'SKIP', 9, 25, '2026-06-22 22:41:11'::timestamptz, 39.953412, -75.161221),

  -- Sarah on new videos
  (2, '7d3b9a12-6e4c-4a91-9c8f-2f7d5b1a3001'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-20 09:18:00'::timestamptz, 40.714221, -74.006881),
  (2, '7d3b9a12-6e4c-4a91-9c8f-2f7d5b1a3001'::uuid, 'PLAY', NULL, 38, '2026-06-20 09:18:02'::timestamptz, 40.714221, -74.006881),
  (2, '7d3b9a12-6e4c-4a91-9c8f-2f7d5b1a3001'::uuid, 'WATCH_25', 10, 38, '2026-06-20 09:18:12'::timestamptz, 40.714221, -74.006881),
  (2, '7d3b9a12-6e4c-4a91-9c8f-2f7d5b1a3001'::uuid, 'WATCH_50', 19, 38, '2026-06-20 09:18:21'::timestamptz, 40.714221, -74.006881),
  (2, '7d3b9a12-6e4c-4a91-9c8f-2f7d5b1a3001'::uuid, 'WATCH_75', 29, 38, '2026-06-20 09:18:31'::timestamptz, 40.714221, -74.006881),
  (2, '7d3b9a12-6e4c-4a91-9c8f-2f7d5b1a3001'::uuid, 'WATCH_COMPLETE', 38, 38, '2026-06-20 09:18:40'::timestamptz, 40.714221, -74.006881),
  (2, '7d3b9a12-6e4c-4a91-9c8f-2f7d5b1a3001'::uuid, 'LIKE', NULL, 38, '2026-06-20 09:18:45'::timestamptz, 40.714221, -74.006881),
  (2, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-21 12:31:00'::timestamptz, 40.721221, -74.009112),
  (2, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'PLAY', NULL, 55, '2026-06-21 12:31:02'::timestamptz, 40.721221, -74.009112),
  (2, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'WATCH_25', 14, 55, '2026-06-21 12:31:16'::timestamptz, 40.721221, -74.009112),
  (2, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'WATCH_50', 27, 55, '2026-06-21 12:31:29'::timestamptz, 40.721221, -74.009112),
  (2, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'WATCH_75', 41, 55, '2026-06-21 12:31:43'::timestamptz, 40.721221, -74.009112),
  (2, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'WATCH_COMPLETE', 55, 55, '2026-06-21 12:31:57'::timestamptz, 40.721221, -74.009112),
  (2, 'c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 'COMMENT', NULL, 55, '2026-06-21 12:32:05'::timestamptz, 40.721221, -74.009112),
  (2, 'b36c8d61-47a9-4d5a-b6f4-1c3e6a8b8006'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-22 21:14:00'::timestamptz, 40.716821, -74.005321),
  (2, 'b36c8d61-47a9-4d5a-b6f4-1c3e6a8b8006'::uuid, 'PLAY', NULL, 25, '2026-06-22 21:14:02'::timestamptz, 40.716821, -74.005321),
  (2, 'b36c8d61-47a9-4d5a-b6f4-1c3e6a8b8006'::uuid, 'WATCH_25', 6, 25, '2026-06-22 21:14:08'::timestamptz, 40.716821, -74.005321),
  (2, 'b36c8d61-47a9-4d5a-b6f4-1c3e6a8b8006'::uuid, 'SKIP', 8, 25, '2026-06-22 21:14:11'::timestamptz, 40.716821, -74.005321),

  -- Devon on new videos
  (3, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-21 19:02:00'::timestamptz, 34.051221, -118.244991),
  (3, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'PLAY', NULL, 42, '2026-06-21 19:02:02'::timestamptz, 34.051221, -118.244991),
  (3, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'WATCH_25', 10, 42, '2026-06-21 19:02:12'::timestamptz, 34.051221, -118.244991),
  (3, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'WATCH_50', 21, 42, '2026-06-21 19:02:23'::timestamptz, 34.051221, -118.244991),
  (3, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'WATCH_75', 31, 42, '2026-06-21 19:02:33'::timestamptz, 34.051221, -118.244991),
  (3, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'WATCH_COMPLETE', 42, 42, '2026-06-21 19:02:44'::timestamptz, 34.051221, -118.244991),
  (3, 'a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 'LIKE', NULL, 42, '2026-06-21 19:02:48'::timestamptz, 34.051221, -118.244991),
  (3, 'f25a7b50-36f8-4c49-a5e3-9b2d5f7a7005'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-22 17:51:00'::timestamptz, 34.048221, -118.250112),
  (3, 'f25a7b50-36f8-4c49-a5e3-9b2d5f7a7005'::uuid, 'PLAY', NULL, 48, '2026-06-22 17:51:03'::timestamptz, 34.048221, -118.250112),
  (3, 'f25a7b50-36f8-4c49-a5e3-9b2d5f7a7005'::uuid, 'WATCH_25', 12, 48, '2026-06-22 17:51:15'::timestamptz, 34.048221, -118.250112),
  (3, 'f25a7b50-36f8-4c49-a5e3-9b2d5f7a7005'::uuid, 'WATCH_50', 24, 48, '2026-06-22 17:51:27'::timestamptz, 34.048221, -118.250112),
  (3, 'f25a7b50-36f8-4c49-a5e3-9b2d5f7a7005'::uuid, 'WATCH_75', 36, 48, '2026-06-22 17:51:39'::timestamptz, 34.048221, -118.250112),
  (3, 'f25a7b50-36f8-4c49-a5e3-9b2d5f7a7005'::uuid, 'WATCH_COMPLETE', 48, 48, '2026-06-22 17:51:51'::timestamptz, 34.048221, -118.250112),
  (3, 'f25a7b50-36f8-4c49-a5e3-9b2d5f7a7005'::uuid, 'SHARE', NULL, 48, '2026-06-22 17:52:01'::timestamptz, 34.048221, -118.250112),
  (3, 'e14f6a49-25e7-4b38-94f2-8a1c4d6e6004'::uuid, 'IMPRESSION', NULL, NULL, '2026-06-23 18:22:00'::timestamptz, 34.046821, -118.245321),
  (3, 'e14f6a49-25e7-4b38-94f2-8a1c4d6e6004'::uuid, 'PLAY', NULL, 33, '2026-06-23 18:22:02'::timestamptz, 34.046821, -118.245321),
  (3, 'e14f6a49-25e7-4b38-94f2-8a1c4d6e6004'::uuid, 'WATCH_25', 8, 33, '2026-06-23 18:22:10'::timestamptz, 34.046821, -118.245321),
  (3, 'e14f6a49-25e7-4b38-94f2-8a1c4d6e6004'::uuid, 'SKIP', 10, 33, '2026-06-23 18:22:13'::timestamptz, 34.046821, -118.245321);

-- ============================================================
-- VIDEO LIKES
-- ============================================================
INSERT INTO curvy_dolphin.video_likes
  (video_id, user_id, created_at)
VALUES
  ('b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 1, '2026-06-17 12:05:00'::timestamptz),
  ('fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 2, '2026-06-17 13:10:00'::timestamptz),
  ('50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 3, '2026-06-18 09:20:00'::timestamptz),
  ('ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 3, '2026-06-18 10:00:00'::timestamptz),
  ('3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 2, '2026-06-18 16:40:00'::timestamptz),

  ('fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 4, '2026-06-19 18:43:06'::timestamptz),
  ('50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 5, '2026-06-19 09:12:36'::timestamptz),
  ('89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 6, '2026-06-21 15:47:56'::timestamptz),
  ('b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 5, '2026-06-21 21:06:07'::timestamptz),
  ('ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 6, '2026-06-22 19:42:47'::timestamptz),
  ('7d3b9a12-6e4c-4a91-9c8f-2f7d5b1a3001'::uuid, 2, '2026-06-20 09:18:45'::timestamptz),
  ('a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 1, '2026-06-19 20:11:48'::timestamptz),
  ('a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 3, '2026-06-21 19:02:48'::timestamptz),
  ('c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 1, '2026-06-20 16:28:42'::timestamptz),
  ('c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 2, '2026-06-21 12:32:12'::timestamptz),
  ('e14f6a49-25e7-4b38-94f2-8a1c4d6e6004'::uuid, 6, '2026-06-22 13:05:39'::timestamptz),
  ('f25a7b50-36f8-4c49-a5e3-9b2d5f7a7005'::uuid, 3, '2026-06-22 17:52:08'::timestamptz),
  ('b36c8d61-47a9-4d5a-b6f4-1c3e6a8b8006'::uuid, 4, '2026-06-23 09:35:00'::timestamptz);

-- ============================================================
-- VIDEO SHARES
-- ============================================================
INSERT INTO curvy_dolphin.video_shares
  (video_id, user_id, created_at)
VALUES
  ('b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 1, '2026-06-17 12:06:30'::timestamptz),
  ('89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 1, '2026-06-19 08:45:00'::timestamptz),
  ('fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 2, '2026-06-19 21:15:00'::timestamptz),
  ('b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 5, '2026-06-21 21:06:09'::timestamptz),
  ('c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 1, '2026-06-20 16:28:34'::timestamptz),
  ('f25a7b50-36f8-4c49-a5e3-9b2d5f7a7005'::uuid, 3, '2026-06-22 17:52:01'::timestamptz);

-- ============================================================
-- COMMENTS
-- ============================================================
INSERT INTO curvy_dolphin.comments
  (video_id, user_id, text, created_at)
VALUES
  ('b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 1, 'This view is unreal, where was this taken?', '2026-06-17 12:04:00'::timestamptz),
  ('50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 3, 'Ok now I need to know what coffee shop this is', '2026-06-18 09:25:00'::timestamptz),
  ('ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 3, 'That pasta looks incredible, recipe please!', '2026-06-18 10:02:00'::timestamptz),
  ('3487cddb-ec03-480a-aca2-71318a2bb58f'::uuid, 2, 'Adding this beach to my list immediately', '2026-06-18 16:42:00'::timestamptz),

  ('fb8fe72f-947e-4e7f-95b4-19828bb43559'::uuid, 4, 'That sunset is incredible. Philly has some great views.', '2026-06-19 18:43:20'::timestamptz),
  ('50ac1eaf-aa95-4e4e-94c6-72b44a48d283'::uuid, 5, 'Adding this place to my coffee list immediately.', '2026-06-19 09:13:01'::timestamptz),
  ('89e0bfe4-fc43-481b-8649-1f699034c8d4'::uuid, 6, 'This beach looks perfect right now.', '2026-06-21 15:48:12'::timestamptz),
  ('b745cfb4-0b43-4fe4-9a0a-1970b528c6ff'::uuid, 5, 'The skyline looks so good at night.', '2026-06-21 21:06:22'::timestamptz),
  ('ea4e8dc5-9532-49e2-8426-65f41f505a24'::uuid, 6, 'I need the recipe for this.', '2026-06-22 19:43:05'::timestamptz),
  ('a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 1, 'Okay, that pizza looks amazing.', '2026-06-19 20:12:03'::timestamptz),
  ('a81c4e27-93b5-4f16-b2d9-6c7e8a2f4002'::uuid, 3, 'Where is this? I need to try it.', '2026-06-21 19:03:10'::timestamptz),
  ('c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 1, 'I haven''t been to Pittsburgh in years. Looks great.', '2026-06-20 16:29:01'::timestamptz),
  ('c92e5f38-14d6-4a27-83e1-7f9b3c5d5003'::uuid, 2, 'That view is awesome.', '2026-06-21 12:32:30'::timestamptz),
  ('e14f6a49-25e7-4b38-94f2-8a1c4d6e6004'::uuid, 6, 'That sunset is unreal.', '2026-06-22 13:06:25'::timestamptz),
  ('f25a7b50-36f8-4c49-a5e3-9b2d5f7a7005'::uuid, 3, 'Those mountain views are insane.', '2026-06-22 17:52:25'::timestamptz),
  ('b36c8d61-47a9-4d5a-b6f4-1c3e6a8b8006'::uuid, 4, 'Downtown looks completely different at night.', '2026-06-23 09:35:30'::timestamptz);
