using SnapTown.DAL;
using SnapTown.DTO;
using SnapTown.Models;
using SnapTown.WebService.Converters;
using System.Net.Http;
using System.Web.Http;

namespace SnapTown.WebService.Controllers
{
    [RoutePrefix("api/auth")]
    public class AuthController : ApiController
    {
        private IUnitOfWork unitOfWork { get; set; }
        public AuthController(IUnitOfWork unitOfWork)
        {
            this.unitOfWork = unitOfWork;
        }

        [Route("")]
        public void Post(RegisterUserDto registerUser)
        {
            //TODO verify facebook

            //register
            var user = this.unitOfWork.Users.Get(u => u.FacebookId == registerUser.FacebookId);
            if (user != null)
            {
                //update
                user.AuthToken = registerUser.AuthToken;
                user.GCMClientToken = registerUser.GCMClientToken;
                this.unitOfWork.Users.Update(user);
            }
            else
            {
                //register
                var newUser = UserConverter.AsUser.Compile().Invoke(registerUser);
                this.unitOfWork.Users.Create(newUser);
            }

            this.unitOfWork.SaveChanges();
        }
    }
}
