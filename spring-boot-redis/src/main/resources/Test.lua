local isExist = redis.call ('EXISTS', KEYS [1])
if isExist == 0 then
  return '3'
end
local setSize = redis.call ('SCARD', KEYS [1])
local total = tonumber(ARGV[1])
if setSize < total then
    local res = redis.call ('SADD', KEYS [1] ,ARGV[2])
    if res == 0 then
     return '2'
    end
  return '0'
else
  return '1'
end