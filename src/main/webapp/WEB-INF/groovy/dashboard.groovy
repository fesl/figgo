import br.octahedron.straight.modules.users.data.User
request.user = new User("test@example.com", "Nome", "+55 83 8888 8888", "http://lorempixum.com/100/100/", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.")

render 'user/index.vm', request, response