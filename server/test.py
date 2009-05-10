
import xmlrpclib as rpc
import socket

try :
	s =	rpc.ServerProxy('http://localhost:8080')

	id = s.Session.start('Guest')
	print id

	print s.Chat.post(id, 'Just a test')

	print s.Session.end(id)
except socket.error, e:
	print e

