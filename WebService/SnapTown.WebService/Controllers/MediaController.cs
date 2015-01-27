using SnapTown.DAL;
using SnapTown.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace SnapTown.WebService.Controllers
{
    public class MediaController : ApiController
    {
        private IUnitOfWork unitOfWork { get; set; }
        public MediaController(IUnitOfWork unitOfWork)
        {
            this.unitOfWork = unitOfWork;
        }

        public IEnumerable<Media> Get()
        {
            return this.unitOfWork.Media.All();
        }
    }
}
